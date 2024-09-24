package com.dopaminedefense.dodiserver.common.annotation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kotlin.reflect.KClass



@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation
@ApiResponses(value = [
    ApiResponse(responseCode = "200")
])
annotation class SwaggerApiSuccess(
    val summary: String = "",
    val implementation: KClass<*>
)