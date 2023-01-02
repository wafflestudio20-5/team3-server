package com.wafflestudio.team03server.core.user.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import javax.mail.internet.MimeMessage

@Service
class EmailService(
    private val mailSender: JavaMailSender,
) {

    fun sendEmail(toMail: String, title: String, content: String) {
        val message: MimeMessage = mailSender.createMimeMessage()
        // true 매개값을 전달하면 multipart 형식의 메세지 전달이 가능.문자 인코딩 설정도 가능하다.
        val helper = MimeMessageHelper(message, true, "utf-8")
        helper.setTo(toMail)
        helper.setSubject(title)
        // true 전달 > html 형식으로 전송 , 작성하지 않으면 단순 텍스트로 전달.
        helper.setText(content, true)
        mailSender.send(message)
    }

    fun sendVerificationEmail(email: String, verificationToken: String) {
        sendEmail(
            email,
            "회원가입 인증 이메일",
            "http://localhost:8080/auth/verifyEmail?token=$verificationToken",
        )
    }
}
