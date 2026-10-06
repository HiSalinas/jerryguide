package com.jsalin.laguiadejerryapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PageInfoDto(
    val count: Int,
    val next: String?,
    val pages: Int,
    val prev: String?
)