package com.dopaminedefense.dodiserver.alarm.repository

import com.dopaminedefense.dodiserver.alarm.dto.AlarmDto
import org.springframework.transaction.annotation.Transactional


interface AlarmDslRepository {

    fun getAlarms(email: String) : List<AlarmDto>

    @Transactional
    fun getAlarmsForNotification(email: String) : List<AlarmDto>
}