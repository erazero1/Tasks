package com.erazero1.tasks.domain.model

import java.io.IOException

sealed class AppNetworkException: IOException() {
    class NoInternetException: AppNetworkException()
    data class ServerException(val code: Int): AppNetworkException()
    data class UnknownNetworkException(override val message: String?): AppNetworkException()
}