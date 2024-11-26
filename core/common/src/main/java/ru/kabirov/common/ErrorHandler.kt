package ru.kabirov.common

import retrofit2.HttpException
import ru.kabirov.common.resource.ResourceManager
import java.net.UnknownHostException
import javax.inject.Inject

class ErrorHandler @Inject constructor(
    private val resourceManager: ResourceManager,
) {
    fun handleError(error: Throwable): Throwable {
        return when(error) {
            is UnknownHostException -> {
                Throwable(resourceManager.getString(R.string.no_internet))
            }
            is HttpException -> {
                NotFoundException()
            }
            else -> Throwable("${resourceManager.getString(R.string.unknown_error)} ${error.message}")
        }
    }

    inner class NotFoundException: Throwable()
}