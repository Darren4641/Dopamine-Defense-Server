package com.dopaminedefense.dodiserver.block.factory.impl

import com.dopaminedefense.dodiserver.block.dto.BlockColor
import com.dopaminedefense.dodiserver.block.entity.Block
import com.dopaminedefense.dodiserver.block.entity.Sync
import com.dopaminedefense.dodiserver.block.factory.SyncBlock

class SyncDodiAction (
    override val syncData: Sync,
    override val block: Block
) : SyncBlock(syncData, block) {
    override fun sync(): Block? {
        val blockFlatList = convertStringToList(block.detailedBlocks).toMutableList()

        var startIndex = blockFlatList.indexOf(0)
        val endIndex = startIndex + syncData.interval
        if(startIndex != -1) {
            while(startIndex < endIndex) {
                blockFlatList[startIndex++] = BlockColor.Blue.value
            }
        }

        block.detailedBlocks = convertListToString(blockFlatList)
        block.actionCount += 1
        return block
    }
}