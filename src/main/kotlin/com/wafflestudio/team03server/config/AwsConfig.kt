package com.wafflestudio.team03server.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.wafflestudio.team03server.properties.S3Properties
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(S3Properties::class)
@EnableAutoConfiguration(exclude = [ContextInstanceDataAutoConfiguration::class])
class AwsConfig(
    private val s3Properties: S3Properties
) {

    private val accessKey = s3Properties.credentials.accessKey

    private val secretKey = s3Properties.credentials.secretKey

    private val region = s3Properties.region.static

    @Bean
    fun amazonS3(): AmazonS3 {
        val awsCredentials = BasicAWSCredentials(accessKey, secretKey)
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
            .build()
    }
}
