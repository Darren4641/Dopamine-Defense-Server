package com.dopaminedefense.dodiserver.block.controller

import com.dopaminedefense.dodiserver.block.dto.SyncReq
import com.dopaminedefense.dodiserver.block.service.SyncService
import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiError
import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiSuccess
import com.example.kopring.common.response.BaseResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sync")
@Tag(name = "Sync API", description = "Sync 데이터 Bulk Insert 등을 포함.")
class SyncController(
    val syncService: SyncService
) {


    @SwaggerApiSuccess(summary = "Sync 데이터 삽입", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    [D-02]: 일치하는 데이터가 없습니다.
    """)
    @PostMapping("")
    fun saveSync(@RequestBody @Valid syncReqs: SyncReq) : BaseResponse<*> {
        return BaseResponse(data = syncService.saveSyncData(syncReqs))
    }

}