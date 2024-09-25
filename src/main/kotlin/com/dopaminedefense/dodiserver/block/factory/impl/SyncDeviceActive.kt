package com.dopaminedefense.dodiserver.block.factory.impl

import com.dopaminedefense.dodiserver.block.dto.BlockColor
import com.dopaminedefense.dodiserver.block.dto.Power
import com.dopaminedefense.dodiserver.block.dto.SyncType
import com.dopaminedefense.dodiserver.block.entity.Block
import com.dopaminedefense.dodiserver.block.entity.Block.Companion.BLOCK_SECOND
import com.dopaminedefense.dodiserver.block.entity.Sync
import com.dopaminedefense.dodiserver.block.factory.SyncBlock
import com.dopaminedefense.dodiserver.block.repository.SyncRepository
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.dateTimeFormatter
import java.time.*

class SyncDeviceActive (
    override val syncData: Sync,
    override val block: Block,
    val lastDeviceActiveSync: Sync?,
    val syncRepository: SyncRepository,
    val countryZone: String
) : SyncBlock(syncData, block) {
    override fun sync(): Block? {
        if(syncData.pickup == Power.ON) {
            println("[ON]")
            if(lastDeviceActiveSync != null && lastDeviceActiveSync.pickup == Power.ON) {
                // Current = ON, Last = ON
                val duration = getTimeDifferenceInSeconds(lastDeviceActiveSync.createdDate, syncData.createdDate)
                val offSync = Sync(
                    interval = syncData.interval,
                    pickup = Power.OFF,
                    type = SyncType.DEVICE_ACTIVE,
                    isSync = true,
                    user = syncData.user
                )
                offSync.changeCreateDateTime(getHalfDateTime(lastDeviceActiveSync.createdDate, syncData.createdDate))
                syncRepository.save(offSync)
                block.restSec += duration.dividedBy(2).seconds.toInt()
                // RestMinGraph 계산
                arrangeRestTimeGraph(duration.dividedBy(2).seconds.toInt())
            } else if(lastDeviceActiveSync != null && lastDeviceActiveSync.pickup == Power.OFF) {
                // Current = ON, Last = OFF
                val duration = getTimeDifferenceInSeconds(lastDeviceActiveSync.createdDate, syncData.createdDate)
                block.restSec += duration.seconds.toInt()
                // RestMinGraph 계산
                arrangeRestTimeGraph(duration.seconds.toInt())
            }

            block.pickupOnCount += 1
        } else if(syncData.pickup == Power.OFF) {
            println("[OFF]")
            if(lastDeviceActiveSync != null && lastDeviceActiveSync.pickup == Power.OFF) {
                // Current = OFF, Last = OFF
                val duration = getTimeDifferenceInSeconds(lastDeviceActiveSync.createdDate, syncData.createdDate)
                val offSync = Sync(
                    interval = syncData.interval,
                    pickup = Power.ON,
                    type = SyncType.DEVICE_ACTIVE,
                    isSync = true,
                    user = syncData.user
                )
                offSync.changeCreateDateTime(getHalfDateTime(lastDeviceActiveSync.createdDate, syncData.createdDate))
                syncRepository.save(offSync)
                block.restSec += duration.dividedBy(2).seconds.toInt()
                block.pickupOnCount += 1
                // RestMinGraph 계산
                arrangeRestTimeGraph(duration.dividedBy(2).seconds.toInt())
                // Usage 계산
                calculatedLongestUsage(offSync)
            } else if(lastDeviceActiveSync != null && lastDeviceActiveSync.pickup == Power.ON) {
                // Usage 계산
                calculatedLongestUsage(lastDeviceActiveSync)
            } else if(lastDeviceActiveSync != null && lastDeviceActiveSync.pickup == Power.TER) {
                // Usage 계산
                calculatedLongestUsage(lastDeviceActiveSync)
            }


        } else if(syncData.pickup == Power.TER) {
            println("[TER]")
            if(lastDeviceActiveSync != null && lastDeviceActiveSync.pickup == Power.ON) {
                // Usage 계산
                calculatedLongestUsage(lastDeviceActiveSync)
            }
        }


        //블럭 계산
        calculatedDetailedBlock()

        return block
    }

    private fun arrangeRestTimeGraph(durationRestSec: Int) {
        val localDateTime = LocalDateTime.parse(syncData.localDate, dateTimeFormatter)

        var curHour = localDateTime.hour
        var curMin = localDateTime.minute
        var remainingRestMin = Math.round(durationRestSec / 60.0).toInt()

        var tempRestMinGraph = block.restMinGraph.removeSurrounding("[", "]")
            .split(",")
            .map(String::trim)
            .map(String::toInt)
            .toIntArray()
        if (remainingRestMin >= curMin) {
            tempRestMinGraph[curHour] += curMin
        } else {
            tempRestMinGraph[curHour] += remainingRestMin
        }

        remainingRestMin -= curMin
        while(0 < remainingRestMin && --curHour >= 0) {
            if(60 <= remainingRestMin) {
                tempRestMinGraph[curHour] += 60
            }else {
                tempRestMinGraph[curHour] += remainingRestMin
            }
            remainingRestMin -= 60
        }

        block.restMinGraph = tempRestMinGraph.joinToString(",", "[", "]")
    }

    private fun calculatedDetailedBlock() {
        val blockFlatList = convertStringToList(block.detailedBlocks).toMutableList()
        var count = block.restSec / BLOCK_SECOND

        while(count-- > 0) {
            val startIndex = blockFlatList.indexOf(0)
            if(startIndex != -1) {
                blockFlatList[startIndex] = BlockColor.Green.value
            }
        }

        block.restSec = block.restSec % BLOCK_SECOND
        block.detailedBlocks = convertListToString(blockFlatList)
    }

    private fun calculatedLongestUsage(lastDeviceActiveSync: Sync) {
        val duration = getTimeDifferenceInSeconds(lastDeviceActiveSync.createdDate, syncData.createdDate)
        block.longestUsage = Math.max(block.longestUsage, duration.seconds.toInt())


    }


    private fun getTimeDifferenceInSeconds(lastDateTimeStr: String, currentDateTimeStr: String) : Duration {

        val lastDateTime = LocalDateTime.parse(lastDateTimeStr, dateTimeFormatter)
        val currentDateTime = LocalDateTime.parse(currentDateTimeStr, dateTimeFormatter)

        val duration = Duration.between(lastDateTime, currentDateTime)

        return duration
    }

    private fun getHalfDateTime(lastDateTimeStr: String, currentDateTimeStr: String) : String {
        val lastDateTime = LocalDateTime.parse(lastDateTimeStr, dateTimeFormatter)
        val currentDateTime = LocalDateTime.parse(currentDateTimeStr, dateTimeFormatter)

        val duration = Duration.between(lastDateTime, currentDateTime)

        val halfDuration = duration.dividedBy(2)
        return lastDateTime.plusSeconds(halfDuration.seconds).format(dateTimeFormatter)
    }

}