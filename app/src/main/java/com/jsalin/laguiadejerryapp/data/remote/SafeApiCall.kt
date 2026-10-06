package com.jsalin.laguiadejerryapp.data.remote

import com.jsalin.laguiadejerryapp.domain.model.DataError
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(call: suspend () -> T): T = try {
    call()
} catch (e: IOException) {
    throw DataError.Network(e)
} catch (e: HttpException) {
    throw if (e.code() == HTTP_NOT_FOUND) DataError.NotFound(e) else DataError.Server(e)
}

private const val HTTP_NOT_FOUND = 404