package com.wafflestudio.team03server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
class Team03ServerApplication

fun main(args: Array<String>) {
    runApplication<Team03ServerApplication>(*args)
}
