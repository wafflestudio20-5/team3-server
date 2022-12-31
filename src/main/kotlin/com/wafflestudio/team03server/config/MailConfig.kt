package com.wafflestudio.team03server.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
class MailConfig {

    @Bean
    fun mailSender(
        @Value("\${gmail.username}") username: String,
        @Value("\${gmail.password}") password: String,
    ): JavaMailSender {
        val props = Properties()
        props["mail.smtp.auth"] = true
        props["mail.smtp.starttls.enable"] = true
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = "587"

        val mailSender = JavaMailSenderImpl()
        mailSender.javaMailProperties = props
        mailSender.username = username
        mailSender.password = password
        return mailSender
    }
}
