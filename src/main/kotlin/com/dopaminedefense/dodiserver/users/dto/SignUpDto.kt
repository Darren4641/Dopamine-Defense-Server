package com.dopaminedefense.dodiserver.users.dto

import com.dopaminedefense.dodiserver.users.entity.Users
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*
import kotlin.random.Random

data class SignUpReq(
    @field:NotBlank(message = "email is a required value")
    @field:Email(message = "It's not an e-mail format")
    val email: String,
    @field:NotNull(message = "countryCode is a required value")
    val countryCode: CountryCode,
) {
    fun toEntity() : Users = Users(
        email = email,
        name = generateRandomUsername(),
        countryCode = countryCode.name,
        status = UserStatus.ACTIVE
    )

    private fun generateRandomUsername() : String {
        val prefixes = listOf("Cool", "Super", "Mighty", "Bright", "Fast", "Swift", "Gentle", "Happy")
        val suffixes = listOf("Tiger", "Lion", "Eagle", "Bear", "Wolf", "Panda", "Phoenix", "Falcon")

        val prefix = prefixes.random()
        val suffix = suffixes.random()

        val randomNumber = Random.nextInt(1000, 9999)

        return "$prefix$suffix$randomNumber"
    }
}

data class SignUpRes(
    val id: Long,
    val email: String,
    val utcDateTime: String,
)