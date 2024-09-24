package com.dopaminedefense.dodiserver.alarm.controller

import com.dopaminedefense.dodiserver.alarm.dto.AlarmDto
import com.dopaminedefense.dodiserver.alarm.service.AlarmService
import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiError
import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiSuccess
import com.example.kopring.common.response.BaseResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/alarm")
@Tag(name = "Alarm API", description = "Alarm 보기, Exceed 타임 때 전송할 알림 문구 불러오기 등을 포함.")
class AlarmController (
    val alarmService: AlarmService
) {

    @SwaggerApiSuccess(summary = "Alarm 보기 (호출 시 isRead가 true로 바뀜)", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    """)
    @GetMapping("")
    fun getAlarms(@RequestParam(name = "email") email: String) : BaseResponse<List<AlarmDto>> {
        return BaseResponse(data = alarmService.getAlarms(email))
    }

    @SwaggerApiSuccess(summary = "Exceed 타임 때 전송할 알림 문구 불러오기 호출 시 lastSendTime값이 바뀜", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    """)
    @GetMapping("/notification")
    fun getAlarmsForNotification(@RequestParam(name = "email") email: String) : BaseResponse<List<AlarmDto>> {
        return BaseResponse(data = alarmService.getAlarmsForNotification(email))
    }

}