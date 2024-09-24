package com.dopaminedefense.dodiserver.block.factory

import com.dopaminedefense.dodiserver.block.dto.BlockColor
import com.dopaminedefense.dodiserver.block.entity.Block
import com.dopaminedefense.dodiserver.block.entity.Sync
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class SyncBlock (
    open val syncData: Sync,
    open val block: Block
) {
    abstract fun sync() : Block?

    fun convertStringToList(block: String): List<Int> {
        return block
            .replace("[", "") // 대괄호 제거
            .replace("]", "")
            .split("], [") // 각 행을 분리
            .map { row ->
                row.split(",") // 각 행의 값을 쉼표로 분리
                    .map { it.trim().toInt() } // Int로 변환
            }.flatMap { it }
    }

    fun convertListToString(block: List<Int>): String {
        return block.chunked(9).joinToString(
            prefix = "[[", postfix = "]]", separator = "], ["
        ) { row -> row.joinToString(",") }
            .replace("\\s".toRegex(), "") // 모든 공백 제거
    }

    fun calculatedBlock() {
        val temp = block.detailedBlocks
            .removeSurrounding("[[", "]]")  // 바깥 대괄호 제거
            .split("],[")                  // 각 1차원 배열 분리
            .map { row ->
                row.split(",")
                    .map(String::trim)
                    .map(String::toInt)
                    .toIntArray()
            }
            .toTypedArray()

        var tempBlock = block.blocks
            .removeSurrounding("[", "]")
            .split(",")
            .map(String::trim)
            .map(String::toInt)
            .toIntArray()

        var blockCount = 0

        temp.forEachIndexed { rowIndex, row ->
            val greenBlockCount = row.count { it == BlockColor.Green.value }
            val blueBlockCount = row.count { it == BlockColor.Blue.value }

            tempBlock[rowIndex] = when {
                greenBlockCount + blueBlockCount == 9 -> {
                    blockCount++
                    BlockColor.getBigBlockColor(blueBlockCount)
                }
                else -> BlockColor.Grey.value
            }
        }

        block.blocks = tempBlock.joinToString(",", "[", "]")
        block.blockCount = blockCount
    }

}