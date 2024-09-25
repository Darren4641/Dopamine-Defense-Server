package com.dopaminedefense.dodiserver.alarm.repository

import com.dopaminedefense.dodiserver.alarm.dto.AlarmDto
import com.dopaminedefense.dodiserver.block.dto.Messages
import org.springframework.transaction.annotation.Transactional


interface AlarmDslRepository {

    fun getAlarms(email: String) : List<AlarmDto>

    @Transactional
    fun getAlarmsForNotification(email: String) : List<AlarmDto>

    fun getAlarmsForProfile(email: String, date: String) : List<Messages>
}