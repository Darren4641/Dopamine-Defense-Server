package com.dopaminedefense.dodiserver.alarm.service

import com.dopaminedefense.dodiserver.alarm.dto.AlarmDto

interface AlarmService {

    fun getAlarms(email: String) : List<AlarmDto>

    fun getAlarmsForNotification(email: String) : List<AlarmDto>
}