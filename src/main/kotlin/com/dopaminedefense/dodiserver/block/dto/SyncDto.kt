package com.dopaminedefense.dodiserver.block.dto

import com.dopaminedefense.dodiserver.users.dto.CountryCode

enum class Power {
    ON,
    OFF,
    TER
}

enum class SyncType {
    DEVICE_ACTIVE,
    EXCEED,
    DODI_ACTION
}

class SyncReq (
    val email: String,
    val syncDto: List<SyncDto>
)

class SyncDto (
    val pickup: Power?,
    val type: SyncType?,
    val utcDateTime: String
)

class SyncBulkDto (
    val intervalTime: Int,
    val pickup: Power?,
    val type: SyncType?,
    val isSync: Boolean,
    val utcDateTime: String,
    val countryCode: CountryCode

)

class SyncRes (
    val email: String,
    val pickup: Power?,
    val type: SyncType?,
    val localDateTime: String
)