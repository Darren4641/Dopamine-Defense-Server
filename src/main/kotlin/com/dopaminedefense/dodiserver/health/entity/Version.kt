package com.dopaminedefense.dodiserver.health.entity

import com.dopaminedefense.dodiserver.common.entity.BaseEntity
import com.dopaminedefense.dodiserver.health.dto.VersionRes
import com.dopaminedefense.dodiserver.users.dto.CountryCode
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "version")
class Version (
    @Column(name = "version", nullable = true, length = 320)
    val version : String,

    @Column(name = "is_onboarding", nullable = true, length = 320)
    val isOnboarding : Boolean,

    @Column(name = "is_update")
    val isUpdate : Boolean = false
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun toRes() = VersionRes(version, isOnboarding, isUpdate)
}