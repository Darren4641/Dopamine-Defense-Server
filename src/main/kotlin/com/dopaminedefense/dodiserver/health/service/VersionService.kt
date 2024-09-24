package com.dopaminedefense.dodiserver.health.service

import com.dopaminedefense.dodiserver.health.dto.VersionRes
import com.dopaminedefense.dodiserver.health.entity.Version

interface VersionService {
    fun getLatestVersion() : VersionRes

    fun saveVersion(version: Version) : VersionRes
}