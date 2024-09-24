package com.dopaminedefense.dodiserver.block.factory

import com.dopaminedefense.dodiserver.block.dto.SyncType
import com.dopaminedefense.dodiserver.block.entity.Block
import com.dopaminedefense.dodiserver.block.entity.Sync
import com.dopaminedefense.dodiserver.block.factory.impl.SyncDeviceActive
import com.dopaminedefense.dodiserver.block.factory.impl.SyncDodiAction
import com.dopaminedefense.dodiserver.block.factory.impl.SyncExceed
import com.dopaminedefense.dodiserver.block.repository.SyncRepository
import com.dopaminedefense.dodiserver.common.exception.DodiException
import com.example.kopring.common.status.ResultCode

class BlockFactory {
    companion object {
        fun getSyncedBlock(countryZone: String, syncData: Sync, block: Block, lastDeviceActiveSync: Sync?, syncRepository: SyncRepository) : SyncBlock {
            return when(syncData.type) {
                SyncType.DODI_ACTION -> SyncDodiAction(syncData, block)
                SyncType.EXCEED -> SyncExceed(syncData, block)
                SyncType.DEVICE_ACTIVE -> SyncDeviceActive(syncData, block, lastDeviceActiveSync, syncRepository, countryZone)
                else -> throw DodiException(ResultCode.ERROR)
            }
        }
    }

}