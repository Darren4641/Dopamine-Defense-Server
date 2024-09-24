package com.dopaminedefense.dodiserver.file.service.impl

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.*
import com.amazonaws.util.IOUtils
import com.dopaminedefense.dodiserver.common.properties.S3Properties
import com.dopaminedefense.dodiserver.users.dto.FileDto
import com.dopaminedefense.dodiserver.file.service.FileService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Service
class FileServiceImpl(
    val amazonS3: AmazonS3,
    @Value("\${s3.bucketName}")
    val bucketName: String,
    val s3Properties: S3Properties
) : FileService {


    override fun upload(inputStream: InputStream, fileName: String) : FileDto {
        val metadata = ObjectMetadata()

        val bytes = IOUtils.toByteArray(inputStream)
        metadata.contentLength = bytes.size.toLong()
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        amazonS3.putObject(
            PutObjectRequest(bucketName, fileName, byteArrayInputStream, metadata)
                .withCannedAcl(CannedAccessControlList.Private))

        return FileDto(
            fileName = fileName,
            url = s3Properties.url + "/" + fileName
        )
    }

    override fun getImage(fileName: String): ByteArray {
        val objectRequest = GetObjectRequest(bucketName, fileName)

        val s3Object: S3Object = amazonS3.getObject(objectRequest)
        val objectData: InputStream = s3Object.objectContent

        val byteArrayOutputStream = ByteArrayOutputStream()
        objectData.use { input ->
            byteArrayOutputStream.use { output ->
                input.copyTo(output)
            }
        }
        return byteArrayOutputStream.toByteArray()
    }
}