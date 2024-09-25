package com.dopaminedefense.dodiserver.users.dto

import com.dopaminedefense.dodiserver.users.entity.Users
import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
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
    @Schema(description = "사용자 이메일", example = "abc@gmail.com")
    val email: String,
) {
    fun toEntity() : Users = Users(
        email = email
    )
}

data class SignInRes(
    @Schema(description = "사용자 Auto Id", example = "1")
    val id: Long,
    @Schema(description = "사용자 이메일", example = "abc@gmail.com")
    val email: String,
    @Schema(description = "국가 코드 별 Zone", example = "Asia/Seoul")
    val countryZone: String,
    @Schema(description = "UTC 생성 시간", example = "2024-09-09 12:00:00")
    val utcDateTime: String,
    @Schema(description = "마지막 로그인 시간 (Local Date)", example = "2024-09-09 12:00:00")
    val latestLoginDateTime: String
)