package com.dopaminedefense.dodiserver.health.controller

import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiError
import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiSuccess
import com.dopaminedefense.dodiserver.health.dto.VersionReq
import com.dopaminedefense.dodiserver.health.dto.VersionRes
import com.dopaminedefense.dodiserver.health.service.VersionService
import com.example.kopring.common.response.BaseResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/health")
@Tag(name = "Health Check API", description = "앱 최신버전 및 DB연동 상태 확인")
class VersionController(
    val versionService: VersionService
) {


    @GetMapping("")
    fun healthCheckForAWS() : BaseResponse<String> {
        return BaseResponse(data = "Success")
    }

    @SwaggerApiSuccess(summary = "최신 버전 불러오기", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    [D-02]: 일치하는 데이터가 없습니다.
    """)
    @GetMapping("/version")
    fun getDodiLatestVersion() : BaseResponse<VersionRes> {
        return BaseResponse(data = versionService.getLatestVersion())
    }

    @SwaggerApiSuccess(summary = "버전 저장하기", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    """)
    @PostMapping("")
    fun saveDodiVersion(@RequestBody versionReq: VersionReq) : BaseResponse<VersionRes> {
        return BaseResponse(data = versionService.saveVersion(versionReq.toEntity()))
    }
}