package com.dopaminedefense.dodiserver.health.dto

import com.dopaminedefense.dodiserver.health.entity.Version


data class VersionReq (
    val version: String,
    val isOnboarding: Boolean,
    val isUpdate: Boolean
) {
    fun toEntity() = Version(version, isOnboarding, isUpdate)
}

data class VersionRes (
    val version: String,
    val isOnboarding: Boolean,
    val isUpdate: Boolean
)