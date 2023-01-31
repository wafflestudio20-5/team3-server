package com.wafflestudio.team03server.core.user.api.request

import com.wafflestudio.team03server.core.user.entity.Coordinate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class EditLocationRequest(
    @field:NotBlank(message = "주소는 필수 항목입니다.")
    val location: String?,
    @field:NotNull(message = "좌표 정보가 없습니다.")
    val coordinate: Coordinate?
)
