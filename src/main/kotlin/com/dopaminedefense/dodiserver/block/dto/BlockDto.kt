package com.dopaminedefense.dodiserver.block.dto

import com.dopaminedefense.dodiserver.common.exception.DodiException
import com.example.kopring.common.status.ResultCode
import org.aspectj.weaver.IntMap

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
    val welcomeMessage: String,
    val yesterdayBlock: IntArray,
    val level: Int,
    val interval: Int,
    val blockCount: Int,
    val block: IntArray,
    val detailedBlock: Array<IntArray>,
    val restMinGraph: IntArray,
    val exceededCount: Int,
    val actionCount: Int,
    val longestUsage: Int,
    val restSec: Int,
    var pickupOnCount: Int
)

class CalendarRes (
    val blockDelta: Double,
    val year: Int,
    val month: Int,
    val blockOfCalendar: List<BlockOfCalendar>
)

class BlockOfCalendar (
    val date: Int,
    val level: Int,
    val blockCount: Int,
    val block: IntArray,
    val longestUsage: Int,
    val exceededCount: Int
)