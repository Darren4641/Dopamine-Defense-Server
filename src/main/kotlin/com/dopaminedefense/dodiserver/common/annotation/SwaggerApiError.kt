package com.dopaminedefense.dodiserver.common.annotation

import com.dopaminedefense.dodiserver.common.exception.dto.ExceptionMsg
import com.example.kopring.common.status.ResultCode
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.models.media.Schema
import kotlin.reflect.KClass



@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation
@ApiResponses(value = [
    ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = [Content(mediaType = "application/json", schema = io.swagger.v3.oas.annotations.media.Schema(
            implementation = ExceptionMsg::class))]
    )
])
annotation class SwaggerApiError(
    val description: String
)