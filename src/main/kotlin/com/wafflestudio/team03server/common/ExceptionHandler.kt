package com.wafflestudio.team03server.common

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(value = [RuntimeException::class])
    fun handle(e: Exception) = ResponseEntity<ErrorResponse>(ErrorResponse(e.message!!), e.status)
}
