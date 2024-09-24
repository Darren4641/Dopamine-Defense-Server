package com.dopaminedefense.dodiserver.block.controller

import com.dopaminedefense.dodiserver.block.dto.CalendarRes
import com.dopaminedefense.dodiserver.block.dto.HomeRes
import com.dopaminedefense.dodiserver.block.service.BlockService
import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiError
import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiSuccess
import com.example.kopring.common.response.BaseResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/block")
@Tag(name = "Block API", description = "홈 화면, 캘린더 불러오기 등을 포함.")
class BlockController(
    val blockService: BlockService
) {

    @SwaggerApiSuccess(summary = "홈 화면 (블록 싱크 맞추기)", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    [D-02]: 일치하는 데이터가 없습니다.
    """)
    @GetMapping("/home")
    fun home(@RequestParam(name = "email") email: String) : BaseResponse<HomeRes> {
        return BaseResponse(data = blockService.getHome(email))
    }

    @SwaggerApiSuccess(summary = "캘린더 불러오기", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    [D-02]: 일치하는 데이터가 없습니다.
    """)
    @GetMapping("/calendar")
    fun calendar(@RequestParam(name = "email") email: String, @RequestParam(name = "date") date: String) : BaseResponse<CalendarRes> {
        return BaseResponse(data = blockService.getCalendar(email, date))
    }
}