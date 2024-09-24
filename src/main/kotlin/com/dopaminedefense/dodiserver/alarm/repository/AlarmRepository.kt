package com.dopaminedefense.dodiserver.alarm.repository

import com.dopaminedefense.dodiserver.alarm.entity.Alarm
import org.springframework.data.jpa.repository.JpaRepository

interface AlarmRepository : JpaRepository<Alarm, Long>, AlarmDslRepository {

}