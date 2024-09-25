package com.dopaminedefense.dodiserver.users.dto

import com.google.gson.Gson
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

enum class UserStatus {
    ACTIVE,
    DELETE
}

enum class Level (
  val value: Int
) {
    HARD(9),
    NORMAL(8),
    EASY(7);

    companion object {
        fun fromValue(value: Int): Level? {
            return entries.find { it.value == value }
        }
    }
}

enum class Interval (
    val value: Int
) {
    M15(1),
    M30(2),
    M60(4);

    companion object {
        fun fromValue(value: Int): Interval? {
            return Interval.entries.find { it.value == value }
        }
    }
}

data class ProfileReq (
    @field:NotBlank(message = "email is a required value")
    @field:Email(message = "It's not an e-mail format")
    @Schema(description = "사용자 이메일", example = "abc@gmail.com")
    val email: String,
    @Schema(description = "사용자 이름", example = "Darren")
    val name: String?,
    @field:NotNull(message = "interval is a required value")
    @Schema(description = "도디 시간 간격", example = "M15")
    val interval: Interval,
    @field:NotNull(message = "level is a required value")
    @Schema(description = "도디 난이도", example = "HARD")
    val level: Level,
    @field:NotBlank(message = "job is a required value")
    @Schema(description = "도디 사용 목적", example = "자기 계발")
    val job: String,
    @Schema(description = "프로필 이미지", example = "https://api.dopaminedefense.team/file/image/~~~")
    val image: String?,
    @field:NotBlank(message = "version is a required value")
    @Schema(description = "사용자의 앱 버전", example = "1.0.0")
    val version: String?,
    @field:NotNull(message = "shareStatus is a required value")
    @Schema(description = "사용자의 공유 항목", example = "{\"action\":true,\"exceed\":true,\"pickup\":true,\"longestUsage\":true}")
    val shareStatus: ShareStatus,
    @Schema(description = "사용자의 프로필 딥링크", example = "https://dopaminedefense.team/invite?email=zxz4641@gmail.com")
    val profileLink: String?
)

data class ProfileRes(
    @Schema(description = "사용자 이메일", example = "abc@gmail.com")
    val email: String,
    @Schema(description = "사용자 이름", example = "Darren")
    val name: String,
    @Schema(description = "국가 코드 별 Zone", example = "Asia/Seoul")
    val countryZone: String,
    @Schema(description = "도디 시간 간격", example = "M15")
    val interval: Interval?,
    @Schema(description = "도디 난이도", example = "HARD")
    val level: Level?,
    @Schema(description = "도디 사용 목적", example = "자기 계발")
    val job: String?,
    @Schema(description = "프로필 이미지", example = "https://api.dopaminedefense.team/file/image/~~~")
    val image: String?,
    @Schema(description = "사용자의 앱 버전", example = "1.0.0")
    val version: String?,
    @Schema(description = "온보딩 진행 여부 (false일 시 온보딩페이지로)", example = "true")
    val isOnboarding: Boolean,
    @Schema(description = "사용자의 공유 항목", example = "{\"action\":true,\"exceed\":true,\"pickup\":true,\"longestUsage\":true}")
    val shareStatus: ShareStatus? = null,
    @Schema(description = "사용자의 읽지 않은 알림 수", example = "0")
    val alarmCount: Long,
    @Schema(description = "사용자의 친구 수", example = "1")
    val friendCount: Long?,
    @Schema(description = "사용자의 프로필 딥링크", example = "https://dopaminedefense.team/invite?email=zxz4641@gmail.com")
    val profileLink: String?
) {
    constructor(email: String,
                name: String,
                countryZone: String,
                interval: Int,
                level: Int,
                job: String,
                image: String?,
                version: String?,
                isOnboarding: Boolean,
                shareStatus: String,
                alarmCount: Long,
                friendCount: Long,
                profileLink: String?) : this(
        email = email,
        name = name,
        countryZone = CountryCode.valueOf(countryZone).zone,
        interval = Interval.fromValue(interval),
        level = Level.fromValue(level),
        job = job,
        image = image,
        version = version,
        isOnboarding = isOnboarding,
        shareStatus = Gson().fromJson(shareStatus, ShareStatus::class.java),
        alarmCount = alarmCount,
        friendCount = friendCount,
        profileLink = profileLink)
}

data class ShareStatus (
    val action: Boolean,
    val exceed: Boolean,
    val pickup: Boolean,
    val longestUsage: Boolean
)
