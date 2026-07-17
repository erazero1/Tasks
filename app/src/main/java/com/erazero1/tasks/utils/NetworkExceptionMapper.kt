package com.erazero1.tasks.utils

import com.erazero1.tasks.domain.model.AppNetworkException
import retrofit2.HttpException
import java.io.IOException

fun mapToAppNetworkException(e: Exception): AppNetworkException {
    return when (e) {
        is IOException -> AppNetworkException.NoInternetException()
        is HttpException -> AppNetworkException.ServerException(e.code())
        else -> AppNetworkException.UnknownNetworkException(e.localizedMessage)
    }
}
