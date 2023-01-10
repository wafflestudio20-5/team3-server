package com.wafflestudio.team03server.utils

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class S3Service(private val amazonS3: AmazonS3) {

    @Value("\${cloud.aws.s3.bucket}")
    private lateinit var bucket: String

    fun upload(multipartFile: MultipartFile): String {
        val s3FileName = UUID.randomUUID().toString() + "-" + multipartFile.originalFilename

        val objMeta = ObjectMetadata()
        objMeta.contentLength = multipartFile.size

        amazonS3.putObject(bucket, s3FileName, multipartFile.inputStream, objMeta)

        return amazonS3.getUrl(bucket, s3FileName).toString()
    }
}
