package com.dopaminedefense.dodiserver.block.dto

import com.dopaminedefense.dodiserver.users.dto.CountryCode
import com.dopaminedefense.dodiserver.users.dto.Interval
import io.swagger.v3.oas.annotations.media.Schema

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
    @Schema(description = "사용자 이메일", example = "abc@gmail.com")
    val email: String,
    val syncDto: List<SyncDto>
)

class SyncDto (
    @Schema(description = "전원 상태 - type이 DEVICE_ACTIVE일때만 ON|OFF 그 외에는 null ", example = "ON")
    val pickup: Power?,
    @Schema(description = "DODI_ACTION (EXCEED시 1분이내 도디액션 시), EXCEED(interval만큼 핸드폰 사용 시), DEVICE_ACTIVE(전원 상태 전송 시)", example = "ON")
    val type: SyncType?,
    @Schema(description = "해당 데이터를 전송한 UTC 시간", example = "2024-09-09 12:00:00")
    val utcDateTime: String
)

class SyncBulkDto (
    val interval: Interval,
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