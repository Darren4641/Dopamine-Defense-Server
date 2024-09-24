package com.dopaminedefense.dodiserver.users.dto

import com.dopaminedefense.dodiserver.users.entity.Users
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.springframework.cglib.core.Local
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import kotlin.random.Random

data class SignInReq(
    @field:NotBlank(message = "email is a required value")
    @field:Email(message = "It's not an e-mail format")
    val email: String,
) {
    fun toEntity() : Users = Users(
        email = email
    )
}

data class SignInRes(
    val id: Long,
    val email: String,
    val countryZone: String,
    val utcDateTime: String,
    val latestLoginDateTime: String
)