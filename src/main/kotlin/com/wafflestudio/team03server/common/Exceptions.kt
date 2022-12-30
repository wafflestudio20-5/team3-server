package com.wafflestudio.team03server.common

import org.springframework.http.HttpStatus

open class Exception(msg: String, val status: HttpStatus) : RuntimeException(msg)

class Exception404(msg: String) : Exception(msg, HttpStatus.NOT_FOUND)
class Exception400(msg: String) : Exception(msg, HttpStatus.BAD_REQUEST)
class Exception401(msg: String) : Exception(msg, HttpStatus.UNAUTHORIZED)
class Exception403(msg: String) : Exception(msg, HttpStatus.FORBIDDEN)
class Exception409(msg: String) : Exception(msg, HttpStatus.CONFLICT)
