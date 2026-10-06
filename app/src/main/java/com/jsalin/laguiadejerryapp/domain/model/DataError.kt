package com.jsalin.laguiadejerryapp.domain.model

sealed class DataError(cause: Throwable) : Exception(cause) {
    class Network(cause: Throwable) : DataError(cause)
    class Server(cause: Throwable) : DataError(cause)
    class NotFound(cause: Throwable) : DataError(cause)
}