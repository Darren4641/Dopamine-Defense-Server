package com.dopaminedefense.dodiserver.block.dto

import com.dopaminedefense.dodiserver.common.exception.DodiException
import com.dopaminedefense.dodiserver.users.dto.CountryCode
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.convertUtcStrToLocalDateTime
import com.example.kopring.common.status.ResultCode
import io.swagger.v3.oas.annotations.media.Schema

enum class BlockColor (
    val value: Int
) {


    Grey(0),
    Green(1),
    MostlyGreen(2),
    HalfGreen(3),
    Blue(4);

    companion object {
        fun getBigBlockColor(blueBlockCount: Int) : Int {
            return when(blueBlockCount) {
                0 -> BlockColor.Green.value
                1, 2, 3 -> BlockColor.MostlyGreen.value
                4, 5, 6 -> BlockColor.HalfGreen.value
                7, 8, 9 -> BlockColor.Blue.value
                else -> throw DodiException(ResultCode.ERROR)
            }
        }
    }

}

class HomeRes (
    @Schema(description = "사용자 Local Date 날짜", example = "2024-09-09")
    val date: String,
    @Schema(description = "상단 응원 메시지", example = "5분 휴식하면\\\\n블록 1개 완성 가능!")
    val welcomeMessage: String,
    @Schema(description = "어제 날짜 큰 블럭 배열", example = "[1,0,0,0,0,0,0,0,0]")
    val yesterdayBlock: IntArray,
    @Schema(description = "도디 난이도", example = "HARD")
    val level: Int,
    @Schema(description = "도디 시간 간격", example = "M15")
    val interval: Int,
    @Schema(description = "오늘 큰 블럭 갯수", example = "4")
    val blockCount: Int,
    @Schema(description = "큰 블럭 배열", example = "[1,1,1,1,0,0,0,0,0]")
    val block: IntArray,
    @Schema(description = "작은 블럭 배열", example = "[[1,1,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]]")
    val detailedBlock: Array<IntArray>,
    @Schema(description = "휴식 시간 배열 인덱스 = 해장 지역 시간", example = "[0,0,0,0,0,0,0,0,0,0,0,0,0,45,0,0,0,0,0,0,0,0,0]")
    val restMinGraph: IntArray,
    @Schema(description = "Exceed 수", example = "1")
    val exceedCount: Int,
    @Schema(description = "Dodi Action 수", example = "1")
    val actionCount: Int,
    @Schema(description = "핸드폰 가장 오래한 시간 초 단위", example = "3600")
    val longestUsage: Int,
    @Schema(description = "누적 휴식 시간 초", example = "60")
    val restSec: Int,
    @Schema(description = "전원 껏다 킨 횟수", example = "6")
    var pickupOnCount: Int,
    @Schema(description = "사용자 Local Date 기준 오늘 받은 메시지")
    val messages: List<Messages>
)

class Messages (
    @Schema(description = "메시지 보낸 사람 이메일", example = "abc@gmail.com")
    val email: String,
    @Schema(description = "메시지 보낸 사람 이름", example = "Darren")
    val name: String,
    @Schema(description = "메시지 보낸 사람 프로필 이미지", example = "https://api.dopaminedefense.team/file/image/~~~")
    val image: String?,
    @Schema(description = "Exceed시 메시지를 받았으면 true, 안 받았으면 false(블라 처리)", example = "true")
    val isSend: Boolean,
    @Schema(description = "메시지 내용", example = "이 알림은 받는 친구의 Exceed 시간에 전송됩니다~")
    val content: String,
    @Schema(description = "사용자 Local Date 기준 받은 시간", example = "2024-09-09 12:10:00")
    var sendDate: String?
) {
    fun calSendDate(countryCode: CountryCode) : Messages {
        if(sendDate != null) {
            this.sendDate = convertUtcStrToLocalDateTime(sendDate!!, countryCode)
        }
        return this
    }
}

class CalendarRes (
    @Schema(description = "오늘 큰 블럭 비율 - 어제 큰 블럭 비율", example = "0.0")
    val blockDelta: Double,
    @Schema(description = "년도", example = "2024")
    val year: Int,
    @Schema(description = "월 (1~12)", example = "1")
    val month: Int,
    val blockOfCalendar: List<BlockOfCalendar>
)

class BlockOfCalendar (
    @Schema(description = "일 (1~31)", example = "1")
    val date: Int,
    @Schema(description = "도디 난이도", example = "HARD")
    val level: Int,
    @Schema(description = "해당 날짜 큰 블럭 갯수", example = "4")
    val blockCount: Int,
    @Schema(description = "해당 날짜 큰 블럭 배열", example = "[1,1,1,1,0,0,0,0,0]")
    val block: IntArray,
    @Schema(description = "해당 날짜 핸드폰 가장 오래한 시간 초 단위", example = "3600")
    val longestUsage: Int,
    @Schema(description = "해당 날짜 Exceed 수", example = "1")
    val exceedCount: Int
)