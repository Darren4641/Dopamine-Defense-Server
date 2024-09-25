package com.dopaminedefense.dodiserver.users.dto

import com.dopaminedefense.dodiserver.block.service.impl.BlockServiceImpl.Companion.DEFAULT_BLOCK
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.convertUtcStrToLocalDate
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.convertUtcStrToLocalDateTime
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.convertUtcToLocalTime
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.toDateTime
import com.google.gson.Gson
import io.swagger.v3.oas.annotations.media.Schema
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
    @Schema(description = "사용자 Auto Id", example = "1")
    val id: Long,
    @Schema(description = "사용자 이름", example = "Darren")
    val name: String,
    @Schema(description = "프로필 이미지", example = "https://api.dopaminedefense.team/file/image/~~~")
    val image: String?,
    @Schema(description = "도디 난이도", example = "HARD")
    val level: Level?,
    @Schema(description = "Exceed 수", example = "1")
    val exceedCount: Int?,
    @Schema(description = "다음 예상 Exceed 시간 (null일 시 오늘 도디 한번도 안들어옴)", example = "2024-09-09 12:15:00")
    val nextSentTime: String?,
    @Schema(description = "마지막으로 메시지 받은 시간 (null일 시 메시지를 받은 적이 없음)", example = "2024-09-09 12:15:00")
    val lastSentTime: String?,
    @Schema(description = "큰 블럭 갯수", example = "4")
    val blockCount: Int?,
    @Schema(description = "큰 블럭 배열", example = "[1,1,1,1,0,0,0,0,0]")
    val block: IntArray?,
    @Schema(description = "핸드폰 전원 킨 횟수", example = "3")
    val pickupOnCount: Int?,
    @Schema(description = "누적 휴식시간 초", example = "60")
    val restSec: Int?,
    @Schema(description = "사용자의 공유 항목", example = "{\"action\":true,\"exceed\":true,\"pickup\":true,\"longestUsage\":true}")
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
    @Schema(description = "사용자 이메일", example = "abc@gmail.com")
    val email: String,
    @field:NotBlank(message = "email is a required value")
    @field:Email(message = "It's not an e-mail format")
    @Schema(description = "친구 이메일", example = "abc_friend@gmail.com")
    val friendEmail: String
)

class AddFriendRes (
    val status: FriendStatus,
    val friendEmail: String
)

class UpdateFriendReq (
    @field:NotBlank(message = "email is a required value")
    @field:Email(message = "It's not an e-mail format")
    @Schema(description = "사용자 이메일", example = "abc@gmail.com")
    val email: String,
    @field:NotBlank(message = "email is a required value")
    @field:Email(message = "It's not an e-mail format")
    @Schema(description = "친구 이메일", example = "abc_friend@gmail.com")
    val friendEmail: String,
    @Schema(description = "친구 상태", example = "INVITED")
    val status: FriendStatus
)

class UpdateFriendRes (
    @Schema(description = "친구 상태", example = "ACCEPTED")
    val status: FriendStatus,
    @Schema(description = "친구 이메일", example = "abc_friend@gmail.com")
    val friendEmail: String
)

class SendMessageReq (
    @field:NotBlank(message = "email is a required value")
    @field:Email(message = "It's not an e-mail format")
    @Schema(description = "사용자 이메일", example = "abc@gmail.com")
    val email: String,
    @Schema(description = "친구 Auto ID", example = "[2,3]")
    val friendId: List<Long>,
    @Schema(description = "보낼 메시지 내용", example = "이 알림은 받는 친구의 Exceed 시간에 전송됩니다~")
    val context: String
)

class SendMessageRes (
    val friendId: List<Long>,
    val context: String
)