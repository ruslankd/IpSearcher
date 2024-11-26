package ru.kabirov.data.model

sealed class RequestResult<out T : Any> {
    class InProgress<T : Any> : RequestResult<T>() {
        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }
    }
    class Error<T : Any>(val error: Throwable) : RequestResult<T>()
    class Success<T : Any>(val data: T) : RequestResult<T>()
}

internal fun <T : Any> Result<T>.toRequestResult(): RequestResult<T> {
    return when {
        isSuccess -> RequestResult.Success(getOrThrow())
        isFailure -> RequestResult.Error(this.exceptionOrNull() ?: throw Throwable())
        else -> error("Impossible branch")
    }
}

internal fun <I : Any, O : Any> RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
    return when (this) {
        is RequestResult.Success -> RequestResult.Success(mapper(data))
        is RequestResult.Error -> RequestResult.Error(error)
        is RequestResult.InProgress -> RequestResult.InProgress()
    }
}