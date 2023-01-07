package com.wafflestudio.team03server.core.user.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "cloud.aws")
data class S3Properties(
    val region: Region,
    val credentials: Credentials
)

class Credentials(
    val accessKey: String,
    val secretKey: String
)

class Region(
    val static: String
)
