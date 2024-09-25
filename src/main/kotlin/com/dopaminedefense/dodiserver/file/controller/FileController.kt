package com.dopaminedefense.dodiserver.file.controller

import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiError
import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiSuccess
import com.dopaminedefense.dodiserver.common.exception.DodiException
import com.dopaminedefense.dodiserver.users.dto.FileDto
import com.dopaminedefense.dodiserver.file.service.FileService
import com.example.kopring.common.response.BaseResponse
import com.example.kopring.common.status.ResultCode
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.time.Duration
import java.util.*

@RestController
@RequestMapping("/file")
@Tag(name = "File API", description = "이미지 업로드, 이미지 캐싱 등을 포함.")
class FileController(
    val fileService: FileService
) {
    private val logger = LoggerFactory.getLogger(FileController::class.java)

    @SwaggerApiSuccess(summary = "이미지 업로드", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    [D-10]: 확장자가 올바르지 않습니다.
    [D-11]: 파일 용량[5MB]이 초과하였습니다.
    """)
    @PostMapping("/upload")
    fun uploadImage(@RequestParam file: MultipartFile) : BaseResponse<FileDto> {
        val fileName = file.originalFilename
        val extension = fileName!!.substringAfterLast('.', "")


        if(!(extension == "png" || extension == "jpeg" || extension == "jpg")) {
            throw DodiException(ResultCode.FILE_EXTENSION)
        }

        return BaseResponse(data = fileService.upload(
            inputStream = file.getInputStream(),
            fileName = UUID.randomUUID().toString() + "_" + fileName))
    }

    @SwaggerApiSuccess(summary = "이미지 불러오기", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    """)
    @Cacheable("images")
    @GetMapping("/image/{fileName}")
    fun getImage(@PathVariable("fileName") fileName: String) : ResponseEntity<ByteArray> {
        logger.info("Fetching image from S3 for file: $fileName")  // 메소드 호출 시마다 로그 출력
        val headers = HttpHeaders().apply {
            contentType = MediaType.IMAGE_JPEG // 필요에 따라 이미지 타입 변경 (예: IMAGE_PNG)
            cacheControl = "public, max-age=${Duration.ofMinutes(5).seconds}" // 캐시 유효기간 설정 (5분)
        }

        val fileBytes = fileService.getImage(fileName)

        return ResponseEntity(fileBytes, headers, HttpStatus.OK)
    }
}