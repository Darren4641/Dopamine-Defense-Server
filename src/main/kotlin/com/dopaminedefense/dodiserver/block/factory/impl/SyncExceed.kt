package com.dopaminedefense.dodiserver.block.factory.impl

import com.dopaminedefense.dodiserver.block.dto.BlockColor
import com.dopaminedefense.dodiserver.block.entity.Block
import com.dopaminedefense.dodiserver.block.entity.Sync
import com.dopaminedefense.dodiserver.block.factory.SyncBlock
import com.dopaminedefense.dodiserver.block.service.impl.BlockServiceImpl.Companion.DEFAULT_DETAIL_BLOCK

class SyncExceed (
    override val syncData: Sync,
    override val block: Block
) : SyncBlock(syncData, block) {
    override fun sync(): Block? {
        val blockFlatList = convertStringToList(block.detailedBlocks).toMutableList()

        var isActionBlock = false
        var startIndex =
            if (blockFlatList.indexOf(0) == -1) {
                // 0이 없으면 리스트의 마지막 인덱스를 사용
                blockFlatList.size - 1
            } else {
                if(blockFlatList.indexOf(0) - 1 <= syncData.interval) {
                    //0의 시작 인덱스가 interval보다 낮으면 0으로 초기화
                    block.detailedBlocks = DEFAULT_DETAIL_BLOCK
                    return block
                } else {
                    // 0이 있으면 0의 바로 이전 인덱스를 사용
                    blockFlatList.indexOf(0) - 1
                }
            }
        var endIndex = syncData.interval

        while(endIndex-- > 0) {
            var index = startIndex--
            if(blockFlatList[index] == BlockColor.Blue.value) {
                //DoDiAction을 한거면 actionCount를 감소해야함
                isActionBlock = true
            }
            blockFlatList[index] = BlockColor.Grey.value
        }
        block.detailedBlocks = convertListToString(blockFlatList)
        block.exceededCount += 1
        block.actionCount = if (isActionBlock) {
            block.actionCount - 1 // actionCount를 1 감소시킨 값을 반환
        } else {
            block.actionCount // 그대로 유지
        }
        return block
    }
}