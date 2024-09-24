package com.dopaminedefense.dodiserver.block.service

import com.dopaminedefense.dodiserver.block.dto.SyncBulkDto
import com.dopaminedefense.dodiserver.block.dto.SyncReq

interface SyncService {

    fun saveSyncData(syncReq: SyncReq) : List<SyncBulkDto>
}