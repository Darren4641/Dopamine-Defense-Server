package com.dopaminedefense.dodiserver.health.controller

import com.example.kopring.common.response.BaseResponse
import org.springframework.web.bind.annotation.*

@RestController
class RootController{


    @GetMapping("/")
    fun healthCheckForAWS() : BaseResponse<String> {
        return BaseResponse(data = "Success")
    }
}