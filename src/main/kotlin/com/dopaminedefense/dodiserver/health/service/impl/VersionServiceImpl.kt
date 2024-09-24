package com.dopaminedefense.dodiserver.health.service.impl

import com.dopaminedefense.dodiserver.common.exception.DodiException
import com.dopaminedefense.dodiserver.health.dto.VersionReq
import com.dopaminedefense.dodiserver.health.dto.VersionRes
import com.dopaminedefense.dodiserver.health.entity.Version
import com.dopaminedefense.dodiserver.health.repository.VersionRepository
import com.dopaminedefense.dodiserver.health.service.VersionService
import com.example.kopring.common.status.ResultCode
import org.springframework.stereotype.Service

@Service
class VersionServiceImpl(
    private val versionRepository: VersionRepository
) : VersionService {

    @Override
    override fun getLatestVersion() : VersionRes {
        return versionRepository.findFirstByOrderByCreatedDateDesc()?.let { VersionRes(
            version = it.version,
            isOnboarding = it.isOnboarding,
            isUpdate = it.isUpdate
        ) } ?: throw DodiException(ResultCode.NOT_FOUND)
    }

    @Override
    override fun saveVersion(version: Version): VersionRes {
        return versionRepository.save(version).toRes()
    }

}