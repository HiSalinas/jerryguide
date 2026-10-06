package com.jsalin.laguiadejerryapp.presentation.components

import androidx.annotation.StringRes
import com.jsalin.laguiadejerryapp.R
import com.jsalin.laguiadejerryapp.domain.model.DataError

@StringRes
fun Throwable.toMessageRes(): Int = when (this) {
    is DataError.Network -> R.string.error_network
    is DataError.Server -> R.string.error_server
    is DataError.NotFound -> R.string.error_not_found
    else -> R.string.error_unknown
}