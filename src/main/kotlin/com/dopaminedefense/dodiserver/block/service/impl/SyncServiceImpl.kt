package com.dopaminedefense.dodiserver.block.service.impl

import com.dopaminedefense.dodiserver.block.dto.SyncBulkDto
import com.dopaminedefense.dodiserver.block.dto.SyncReq
import com.dopaminedefense.dodiserver.block.repository.SyncJDBCRepository
import com.dopaminedefense.dodiserver.block.repository.SyncRepository
import com.dopaminedefense.dodiserver.block.service.SyncService
import com.dopaminedefense.dodiserver.common.exception.DodiException
import com.dopaminedefense.dodiserver.users.dto.CountryCode
import com.dopaminedefense.dodiserver.users.dto.Interval
import com.dopaminedefense.dodiserver.users.repository.user.UsersRepository
import com.example.kopring.common.status.ResultCode
import org.springframework.stereotype.Service

@Service
class SyncServiceImpl(
    val userRepository: UsersRepository,
    val syncRepository: SyncRepository,
    val syncJDBCRepository: SyncJDBCRepository
) : SyncService {


    override fun saveSyncData(syncReq: SyncReq) : List<SyncBulkDto> {
        val user = userRepository.findByEmail(syncReq.email) ?: throw DodiException(ResultCode.NOT_FOUND)

        var syncBulkDtoMap = emptyMap<String, SyncBulkDto>().toMutableMap()

        syncReq.syncDto.forEach { syncData ->
            if(!syncBulkDtoMap.containsKey(syncData.utcDateTime) && !syncRepository.existsByTypeAndPickupAndCreatedDateAndUser_email(syncData.type, syncData.pickup, syncData.utcDateTime, user.email)) {
                syncBulkDtoMap[syncData.utcDateTime] = SyncBulkDto(
                    interval = Interval.fromValue(user.interval!!)!!,
                    pickup = syncData.pickup,
                    type = syncData.type,
                    isSync = false,
                    utcDateTime = syncData.utcDateTime,
                    countryCode = CountryCode.valueOf(user.countryCode!!)
                )
            }
        }
        syncJDBCRepository.syncBulkInsert(user.id!!, syncBulkDtoMap)

        return syncBulkDtoMap.values.toList()
    }

}