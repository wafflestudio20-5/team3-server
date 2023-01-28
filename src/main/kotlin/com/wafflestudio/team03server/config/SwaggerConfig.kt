package com.wafflestudio.team03server.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("api")
            .pathsToMatch("/**")
            .build()
    }

    @Bean
    fun springOpenAPI(): OpenAPI {
        return OpenAPI().info(Info().title("Waffle Market API").description("와플마켓 API 명세서입니다."))
    }
}
