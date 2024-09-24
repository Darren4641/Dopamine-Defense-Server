package com.dopaminedefense.dodiserver.block.service.impl

import com.dopaminedefense.dodiserver.block.dto.BlockOfCalendar
import com.dopaminedefense.dodiserver.block.dto.CalendarRes
import com.dopaminedefense.dodiserver.block.dto.HomeRes
import com.dopaminedefense.dodiserver.block.dto.SyncType
import com.dopaminedefense.dodiserver.block.entity.Block
import com.dopaminedefense.dodiserver.block.entity.Block.Companion.BLOCK_SECOND
import com.dopaminedefense.dodiserver.block.entity.Block.Companion.BLOCK_SECOND_OF_DOUBLE
import com.dopaminedefense.dodiserver.block.factory.BlockFactory
import com.dopaminedefense.dodiserver.block.repository.BlockRepository
import com.dopaminedefense.dodiserver.block.repository.SyncRepository
import com.dopaminedefense.dodiserver.block.service.BlockService
import com.dopaminedefense.dodiserver.common.exception.DodiException
import com.dopaminedefense.dodiserver.system.dto.Category
import com.dopaminedefense.dodiserver.users.dto.CountryCode
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.dateFormatter
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.dateTimeFormatter
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.getTodayLocalDateStr
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.getTodayUtcDateStr
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.getYesterdayLocalDateStr
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.getYesterdayUtcDateStr
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.getYesterdayUtcDateTimeStr
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.toDateTime
import com.dopaminedefense.dodiserver.users.entity.Users
import com.dopaminedefense.dodiserver.users.repository.user.UsersRepository
import com.example.kopring.common.status.ResultCode
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.*
import kotlin.math.roundToInt

@Service
class BlockServiceImpl(
    val userRepository: UsersRepository,
    val blockRepository: BlockRepository,
    val syncRepository: SyncRepository
) : BlockService {

    companion object {
        val DEFAULT_BLOCK = "[0,0,0,0,0,0,0,0,0]"
        val DEFAULT_DETAIL_BLOCK = "[[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]]"
        val DEFAULT_REST_MIN_GRAPH = "[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]"
    }

    @Transactional
    override fun getHome(email: String) : HomeRes {
        val user = userRepository.findByEmail(email) ?: throw DodiException(ResultCode.NOT_FOUND)

        val todayDate = getTodayLocalDateStr(CountryCode.valueOf(user.countryCode!!))
        println("todayDate = ${todayDate}")
        var todayBlock = getBlockFromDateOrSave(email, todayDate, user, false)
        todayBlock.calculated = false
        todayBlock = syncBlockData(user, todayBlock, todayDate)
        val yesterdayBlock = getBlockFromDateOrSave(email, getYesterdayLocalDateStr(CountryCode.valueOf(user.countryCode!!)), user, true)

        val welcomeMessage = blockRepository.getWelcomeMessage(email, Category.WELCOME_MSG)

        var message = welcomeMessage.context
        val languageCode = welcomeMessage.languageCode
        if(welcomeMessage.type.contains("compare-to-yesterday")) {
            message = generateCompareToYesterdayMessage(message, languageCode, todayBlock, yesterdayBlock)
        } else if(welcomeMessage.type.contains("promote-action")) {
            message = generatePromoteActionMessage(message, todayBlock)
        }

        return HomeRes(
            welcomeMessage = message,
            yesterdayBlock = yesterdayBlock.blocks.removeSurrounding("[", "]")  // 양쪽의 대괄호 제거
                .split(",")                   // 쉼표로 분리
                .map { it.trim().toInt() }     // 각 요소를 정수로 변환
                .toIntArray(),
            level = user.level!!,
            interval = user.intervalTime!!,
            blockCount = todayBlock.blockCount,
            block = todayBlock.blocks.removeSurrounding("[", "]")  // 양쪽의 대괄호 제거
                .split(",")                   // 쉼표로 분리
                .map { it.trim().toInt() }     // 각 요소를 정수로 변환
                .toIntArray(),
            detailedBlock = todayBlock.detailedBlocks.removeSurrounding("[[", "]]")  // 바깥쪽 대괄호 제거
                .split("],[")                  // 각 1차원 배열 분리
                .map { row ->
                    row.split(",")              // 각 요소를 ','로 분리
                        .map { it.trim().toInt() } // 요소를 정수로 변환
                        .toIntArray()             // IntArray로 변환
                }
                .toTypedArray(),
            restMinGraph = todayBlock.restMinGraph.removeSurrounding("[", "]")  // 양쪽의 대괄호 제거
                .split(",")                   // 쉼표로 분리
                .map { it.trim().toInt() }     // 각 요소를 정수로 변환
                .toIntArray(),
            exceededCount = todayBlock.exceededCount,
            actionCount = todayBlock.actionCount,
            longestUsage = todayBlock.longestUsage,
            restSec = todayBlock.restSec,
            pickupOnCount = todayBlock.pickupOnCount
        )
    }


    @Transactional
    override fun getCalendar(email: String, date: String): CalendarRes {
        val user = userRepository.findByEmail(email) ?: throw DodiException(ResultCode.NOT_FOUND)

        val blockOfMonthCalculateList = blockRepository.getBlockOfMonthByCalculatedIsFalse(email, date)
        blockOfMonthCalculateList.forEach{block ->
            println("createdDate = ${toDateTime(block.createdDate).format(dateFormatter)}")
            syncBlockData(user, block, toDateTime(block.createdDate).format(dateFormatter))
            block.calculated = true
        }
        val (startDate, endDate) = getStartAndEndDates(date)
        val dateSplit = date.split("-")

        val blockOfMonthList = blockRepository.getBlockOfMonth(email, date)

        val allDates = startDate.datesUntil(endDate.plusDays(1)).toList()

        val resultMap = blockOfMonthList.associateBy {
            val dateTime = ZonedDateTime.of(LocalDateTime.parse(it.createdDate, CountryCode.dateTimeFormatter), ZoneOffset.UTC)
            val localZonedDateStr = dateTime.withZoneSameInstant(ZoneId.of(CountryCode.valueOf(user.countryCode!!).zone)).format(dateFormatter)
            println("localZonedTimeStr = ${localZonedDateStr.split("-")[2]}")
            localZonedDateStr.split("-")[2]
        }

        val blockOfCalendar: List<BlockOfCalendar> = allDates.map { date ->
            val dateStr = date.toString().split("-")[2]
            resultMap[dateStr]?.let { blockOfMonth ->
                // BlockOfMonth를 BlockOfCalendar로 변환
                println("dateStr = ${dateStr}")
                BlockOfCalendar(
                    date = dateStr.toInt(),
                    level = user.level!!, // user.level은 nullable이 아니어야 함
                    blockCount = blockOfMonth.blockCount, // blockOfMonth에서 값 추출
                    block = blockOfMonth.blocks.removeSurrounding("[", "]")
                        .split(",")
                        .map { it.trim().toInt() }
                        .toIntArray(),           // 필요한 값들을 추출하여 사용
                    longestUsage = blockOfMonth.longestUsage,
                    exceededCount = blockOfMonth.exceededCount
                )
            } ?: BlockOfCalendar(
                date = dateStr.toInt(), // 해당 날짜가 없는 경우 기본값으로 0 채우기
                level = user.level!!,
                blockCount = 0,
                block = DEFAULT_BLOCK.removeSurrounding("[", "]")
                    .split(",")
                    .map { it.trim().toInt() }
                    .toIntArray(),
                longestUsage = 0,
                exceededCount = 0
            )
        }

        val blockDelta = blockRepository.getBlockCountDifference(currentDate = date, previousDate = getPreviousMonth(date))
        return CalendarRes(
            blockDelta = blockDelta,
            year = dateSplit[0].toInt(),
            month = dateSplit[1].toInt(),
            blockOfCalendar = blockOfCalendar
        )
    }

    private fun syncBlockData(user: Users, block: Block, todayLocalDateStr: String) : Block {
        val userSyncDataList = syncRepository.findByUser_emailAndIsSyncAndLocalDateContainingOrderByCreatedDateAsc(user.email, false, todayLocalDateStr)
        var blockTemp = block
        userSyncDataList.forEach { syncData ->
            val lastDeviceActiveSync = syncRepository.findTopByUser_emailAndIsSyncAndLocalDateContainingAndTypeOrderByCreatedDateDesc(user.email, true, todayLocalDateStr, SyncType.DEVICE_ACTIVE)

            val syncedBlock = BlockFactory.getSyncedBlock(CountryCode.valueOf(user.countryCode!!).zone, syncData, blockTemp, lastDeviceActiveSync, syncRepository)

            syncedBlock.sync()?.let {
                blockTemp = it
                syncData.syncComplete()
                syncRepository.save(syncData)
            }

            syncedBlock.calculatedBlock()
        }
        blockRepository.save(blockTemp)
        return blockTemp
    }

    //특정 날짜 Block 데이터 가져오기 없으면 0으로 생성
    private fun getBlockFromDateOrSave(email: String, date: String, user: Users, isYesterday: Boolean) : Block {

        return blockRepository.getBlock(email, date) ?: blockRepository.save(
            Block(
                blockCount = 0,
                longestUsage = 0,
                calculated = false,
                level = user.level,
                blocks = DEFAULT_BLOCK,
                exceededCount = 0,
                actionCount = 0,
                detailedBlocks = DEFAULT_DETAIL_BLOCK,
                restMinGraph = DEFAULT_REST_MIN_GRAPH,
                restSec = 0,
                isSkipped = isYesterday,
                localDate = if(!isYesterday) {
                    ZonedDateTime.of(LocalDateTime.now(), ZoneId.of(CountryCode.valueOf(user.countryCode!!).zone)).format(dateFormatter)
                } else {
                    ZonedDateTime.of(LocalDateTime.now().minusDays(1), ZoneId.of(CountryCode.valueOf(user.countryCode!!).zone)).format(dateFormatter)
                },
                user = user
            ).changeCreateDateTime(isYesterday)
        )
    }


    private fun generateCompareToYesterdayMessage(message: String, languageCode: String, todayBlock: Block, yesterdayBlock: Block) : String {
        val percentage = calculatePercentageChange(todayBlock.restSec, yesterdayBlock.restSec)
        val delta = if(languageCode.contains("KR")) {
            if((Math.round(percentage.toDouble() * BLOCK_SECOND) / BLOCK_SECOND_OF_DOUBLE) > 0) {
                "늘었"
            } else {
                "줄었"
            }
        } else {
            if((Math.round(percentage.toDouble() * BLOCK_SECOND) / BLOCK_SECOND_OF_DOUBLE) > 0) {
                "increased!"
            } else {
                "decreased!"
            }
        }
        return message.replace("{{percentage}}", percentage)
                .replace("{{delta}}", delta)
    }

    private fun generatePromoteActionMessage(message: String, todayBlock: Block) : String {
        val minutes = calculateMinutesChange(todayBlock.restSec)
        val block = "1"

        return message.replace("{{minutes}}", minutes)
                        .replace("{{blocks}}", block)
    }
    private fun calculatePercentageChange(todayRestSec: Int, yesterdayRestSec: Int): String {
        // 전날 값이 0일 경우를 대비해 처리
        if (yesterdayRestSec == 0) {
            return if (todayRestSec > 0) {
                "100" // 전날 0이고 오늘 값이 있으면 100% 증가로 간주
            } else {
                "0" // 전날과 오늘 모두 0이면 변화 없음
            }
        }

        // 퍼센트 변화 계산
        val change = ((todayRestSec - yesterdayRestSec).toDouble() / yesterdayRestSec) * 100
        return String.format("%.3f", change)
    }

    private fun calculateMinutesChange(todayRestSec: Int) : String {
        return Math.floor((BLOCK_SECOND - todayRestSec) / 60.0).roundToInt().toString()
    }

    private fun getStartAndEndDates(yearMonthStr: String): Pair<LocalDate, LocalDate> {
        val (year, month) = yearMonthStr.split("-").map { it.toInt() } // "2024-09" -> 2024, 9

        // 해당 년/월의 YearMonth 객체 생성
        val yearMonth = YearMonth.of(year, month)

        // 해당 월의 첫 날과 마지막 날 계산
        val startDate = yearMonth.atDay(1) // 월의 첫 번째 날
        val endDate = yearMonth.atEndOfMonth() // 월의 마지막 날

        return startDate to endDate
    }

    private fun getPreviousMonth(yearMonthStr: String): String {
        val yearMonth = YearMonth.parse(yearMonthStr)
        val previousMonth = yearMonth.minusMonths(1)
        return previousMonth.toString()  // 이전 월을 YYYY-MM 형식의 문자열로 반환
    }
}