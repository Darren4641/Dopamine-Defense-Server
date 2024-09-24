package com.dopaminedefense.dodiserver.users.dto

import com.dopaminedefense.dodiserver.block.service.impl.BlockServiceImpl.Companion.DEFAULT_BLOCK
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.convertUtcStrToLocalDate
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.convertUtcStrToLocalDateTime
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.convertUtcToLocalTime
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.toDateTime
import com.google.gson.Gson
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

enum class FriendStatus {
    INVITED,
    ACCEPTED,
    DELETED
}

class FriendRes (
    val friends: List<FriendDto>
)

class FriendDto (
    val id: Long,
    val name: String,
    val image: String?,
    val level: Level?,
    val exceedCount: Int?,
    val nextSentTime: String?,
    val lastSentTime: String?,
    val blockCount: Int?,
    val block: IntArray?,
    val pickupOnCount: Int?,
    val restSec: Int?,
    val shareStatus: ShareStatus
) {
    constructor(id: Long,
                countryCode: String,
                intervalTime: Int,
                name: String,
                image: String,
                level: Int,
                exceedCount: Int?,
                nextSentTime: String?,
                lastSentTime: String?,
                blockCount: Int?,
                block: String?,
                pickupOnCount: Int?,
                restSec: Int?,
                shareStatus: String,
                ) : this(
                    id = id,
                    name = name,
                    image = image,
                    level = Level.fromValue(level),
                    exceedCount = exceedCount ?: 0,
                    nextSentTime = nextSentTime?.let { convertUtcToLocalTime(toDateTime(it).plusSeconds(intervalTime * 60L) , CountryCode.valueOf(countryCode)) },
                    lastSentTime = lastSentTime?.let { convertUtcStrToLocalDateTime(it, CountryCode.valueOf(countryCode)) },
                    blockCount = blockCount ?: 0,
                    block = block?.let { it.removeSurrounding("[", "]")
                        .split(",")
                        .map { it.trim().toInt() }
                        .toIntArray() }
                        ?: DEFAULT_BLOCK.removeSurrounding("[", "]")
                        .split(",")
                        .map { it.trim().toInt() }
                        .toIntArray(),
                    pickupOnCount = pickupOnCount ?: 0,
                    restSec = restSec ?: 0,
                    shareStatus = Gson().fromJson(shareStatus, ShareStatus::class.java)
                )
}

class AddFriendReq (
    @field:NotBlank(message = "email is a required value")
    @field:Email(message = "It's not an e-mail format")
    val email: String,
    @field:NotBlank(message = "email is a required value")
    @field:Email(message = "It's not an e-mail format")
    val friendEmail: String
)

class AddFriendRes (
    val status: FriendStatus,
    val friendEmail: String
)

class UpdateFriendReq (
    @field:NotBlank(message = "email is a required value")
    @field:Email(message = "It's not an e-mail format")
    val email: String,
    @field:NotBlank(message = "email is a required value")
    @field:Email(message = "It's not an e-mail format")
    val friendEmail: String,
    val status: FriendStatus
)

class UpdateFriendRes (
    val status: FriendStatus,
    val friendEmail: String
)

class SendMessageReq (
    @field:NotBlank(message = "email is a required value")
    @field:Email(message = "It's not an e-mail format")
    val email: String,
    val friendId: List<Long>,
    val context: String
)

class SendMessageRes (
    val friendId: List<Long>,
    val context: String
)