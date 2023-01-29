package com.wafflestudio.team03server.core.user.api.request

import com.wafflestudio.team03server.core.user.entity.SearchScope
import javax.validation.constraints.NotNull

data class EditSearchScopeRequest(
    @field:NotNull(message = "검색 범위를 입력하세요.")
    val searchScope: SearchScope?
)
