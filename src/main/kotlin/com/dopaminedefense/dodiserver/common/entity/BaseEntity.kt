package com.dopaminedefense.dodiserver.common.entity

import com.dopaminedefense.dodiserver.users.dto.CountryCode
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.dateTimeFormatter
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.utcFormatter
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity (
    @CreatedDate
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var createdDate : String,

    @LastModifiedDate
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var updatedDate : String,

) {
    constructor(countryCode: CountryCode) : this (
        createdDate = OffsetDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter),
        updatedDate = OffsetDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter)
    )

    constructor() : this (
        createdDate = OffsetDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter),
        updatedDate = OffsetDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter)
    )
}