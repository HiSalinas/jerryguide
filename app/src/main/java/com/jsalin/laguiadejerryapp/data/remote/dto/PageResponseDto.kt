package com.jsalin.laguiadejerryapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PageResponseDto<T>(
    val info: PageInfoDto,
    val results: List<T>
)