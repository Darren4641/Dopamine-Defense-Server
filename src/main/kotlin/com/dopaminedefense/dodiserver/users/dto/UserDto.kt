package com.dopaminedefense.dodiserver.users.dto

import com.google.gson.Gson
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
    val email: String,
    val name: String?,
    @field:NotNull(message = "intervalTime is a required value")
    val intervalTime: Interval,
    @field:NotNull(message = "level is a required value")
    val level: Level,
    @field:NotBlank(message = "job is a required value")
    val job: String,
    val image: String?,
    @field:NotBlank(message = "version is a required value")
    val version: String?,
    @field:NotNull(message = "shareStatus is a required value")
    val shareStatus: ShareStatus,
    val profileLink: String?
)

data class ProfileRes(
    val email: String,
    val name: String,
    val countryZone: String,
    val intervalTime: Interval?,
    val level: Level?,
    val job: String?,
    val image: String?,
    val version: String?,
    val isOnboarding: Boolean,
    val shareStatus: ShareStatus? = null,
    val alarmCount: Long
) {
    constructor(email: String,
                name: String,
                countryZone: String,
                intervalTime: Int,
                level: Int,
                job: String,
                image: String?,
                version: String?,
                isOnboarding: Boolean,
                shareStatus: String,
                alarmCount: Long) : this(
        email = email,
        name = name,
        countryZone = CountryCode.valueOf(countryZone).zone,
        intervalTime = Interval.fromValue(intervalTime),
        level = Level.fromValue(level),
        job = job,
        image = image,
        version = version,
        isOnboarding = isOnboarding,
        shareStatus = Gson().fromJson(shareStatus, ShareStatus::class.java),
        alarmCount = alarmCount)
}

data class ShareStatus (
    val action: Boolean,
    val exceed: Boolean,
    val pickup: Boolean,
    val longestUsage: Boolean
)
