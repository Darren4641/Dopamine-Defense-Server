package com.dopaminedefense.dodiserver.health.repository

import com.dopaminedefense.dodiserver.health.entity.Version
import org.springframework.data.jpa.repository.JpaRepository

interface VersionRepository : JpaRepository<Version, Long>{
    fun findFirstByOrderByCreatedDateDesc() : Version?
}