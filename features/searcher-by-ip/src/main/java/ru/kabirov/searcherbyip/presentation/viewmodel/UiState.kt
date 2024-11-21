package ru.kabirov.searcherbyip.presentation.viewmodel

import ru.kabirov.data.RequestResult
import ru.kabirov.data.model.Subnet

sealed class UiState {
    data object None : UiState()

    data object Loading : UiState()

    class Error(val error: Throwable) : UiState()

    class Success(val subnet: Subnet) : UiState()
}

internal fun RequestResult<Subnet>.toUiState(): UiState {
    return when (this) {
        is RequestResult.Error -> UiState.Error(error)
        is RequestResult.InProgress -> UiState.Loading
        is RequestResult.Success -> UiState.Success(data)
    }
}