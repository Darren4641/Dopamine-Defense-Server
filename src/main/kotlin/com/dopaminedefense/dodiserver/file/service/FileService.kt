package com.dopaminedefense.dodiserver.file.service

import com.dopaminedefense.dodiserver.users.dto.FileDto
import org.springframework.http.ResponseEntity
import java.io.InputStream

interface FileService {

    fun upload(inputStream: InputStream, fileName: String) : FileDto

    fun getImage(fileName: String) : ByteArray
}