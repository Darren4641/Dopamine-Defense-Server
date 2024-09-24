package com.dopaminedefense.dodiserver.block.repository

import com.dopaminedefense.dodiserver.block.dto.Power
import com.dopaminedefense.dodiserver.block.dto.SyncType
import com.dopaminedefense.dodiserver.block.entity.Sync
import org.springframework.data.jpa.repository.JpaRepository

interface SyncRepository : JpaRepository<Sync, Long> {

    fun existsByTypeAndPickupAndCreatedDateAndUser_email(type: SyncType?, pickup: Power?, localDateTime: String, email: String) : Boolean

    fun findByUser_emailAndIsSyncAndLocalDateContainingOrderByCreatedDateAsc(email: String, isSync: Boolean, todayDate: String) : List<Sync>

    fun findTopByUser_emailAndIsSyncAndLocalDateContainingAndTypeOrderByCreatedDateDesc(email: String, isSync: Boolean, localDate: String, type: SyncType) : Sync?

}