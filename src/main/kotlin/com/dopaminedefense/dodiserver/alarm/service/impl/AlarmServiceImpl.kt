package com.dopaminedefense.dodiserver.alarm.service.impl

import com.dopaminedefense.dodiserver.alarm.dto.AlarmDto
import com.dopaminedefense.dodiserver.alarm.repository.AlarmRepository
import com.dopaminedefense.dodiserver.alarm.service.AlarmService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AlarmServiceImpl (
    val alarmRepository: AlarmRepository
) : AlarmService {

    @Transactional
    override fun getAlarms(email: String) : List<AlarmDto> {
        return alarmRepository.getAlarms(email)
    }

    override fun getAlarmsForNotification(email: String) : List<AlarmDto> {
        return alarmRepository.getAlarmsForNotification(email)
    }
}