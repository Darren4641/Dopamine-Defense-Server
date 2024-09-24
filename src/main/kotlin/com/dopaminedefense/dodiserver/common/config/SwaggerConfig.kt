package com.dopaminedefense.dodiserver.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket


@Configuration
@EnableWebMvc
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI? {
        val info: Info = Info()
            .version("v0.0.15")
            .title("도파민 디펜스 API")
            .description("Dodi Application API 명세서")
        return OpenAPI()
            .info(info)
    }

    @Bean
    fun swaggerApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)  // 최신 버전에서는 SWAGGER_2 사용
            .consumes(getConsumeContentTypes())  // 요청 타입 설정
            .produces(getProduceContentTypes())  // 응답 타입 설정
            .apiInfo(swaggerInfo())  // Swagger 정보 설정
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.dopaminedefense.dodiserver"))  // Swagger 적용할 패키지 설정
            .paths(PathSelectors.any())  // 모든 경로에 대해 적용
            .build()
            .useDefaultResponseMessages(false)  // 기본 응답 메시지 비활성화
    }

    private fun swaggerInfo() = ApiInfoBuilder()
        .title("프로젝트 API Specification")
        .description("프로젝트 관련 API 설명서 입니다.")
        .version("1.0.0")
        .build()

    private fun getConsumeContentTypes(): Set<String> {
        return setOf("multipart/form-data")
    }

    private fun getProduceContentTypes(): Set<String> {
        return setOf("application/json;charset=UTF-8")
    }
}