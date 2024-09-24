package com.dopaminedefense.dodiserver.system.controller

import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiError
import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiSuccess
import com.dopaminedefense.dodiserver.system.dto.OperationReq
import com.dopaminedefense.dodiserver.system.dto.OperationRes
import com.dopaminedefense.dodiserver.system.service.OperationService
import com.example.kopring.common.response.BaseResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/operation")
@Tag(name = "알림 문구 API", description = "앱내에 쓰일 문구들을 저장하는 API")
class OperationController(
    val operationService: OperationService
) {

    @SwaggerApiSuccess(summary = "Operation 저장", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    """)
    @PostMapping("/save")
    fun saveOperation(operationReq: OperationReq) : BaseResponse<OperationRes> {
        return BaseResponse(data = operationService.saveOperation(operationReq))
    }
}