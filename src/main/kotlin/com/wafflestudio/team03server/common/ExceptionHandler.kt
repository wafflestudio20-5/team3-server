package com.wafflestudio.team03server.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(value = [Exception::class])
    fun handle(e: Exception) = ResponseEntity<ErrorResponse>(ErrorResponse(e.message!!), e.status)

    // Request Validation
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handle(e: MethodArgumentNotValidException): ResponseEntity<Any> {
        val errors = mutableListOf<String>()
        e.bindingResult.fieldErrors.forEach { fieldError ->
            val errorMessage = fieldError.defaultMessage ?: "값이 유효하지 않습니다."
            errors.add(errorMessage)
        }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }
}
