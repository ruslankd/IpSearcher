package ru.kabirov.common

import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class ErrorHandler @Inject constructor() {
    fun handleError(error: Throwable): Throwable {
        return when(error) {
            is UnknownHostException -> {
                Throwable("Unfortunately, we can't connect to the Internet. Please check your connection and try again.")
            }
            is HttpException -> {
                NotFoundException()
            }
            else -> Throwable("Unknown Error: ${error.message}")
        }
    }

    inner class NotFoundException: Throwable()
}