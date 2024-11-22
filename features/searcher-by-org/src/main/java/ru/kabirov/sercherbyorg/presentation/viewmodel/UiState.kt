package ru.kabirov.sercherbyorg.presentation.viewmodel

import ru.kabirov.data.RequestResult
import ru.kabirov.data.model.Organisation

sealed class UiState {
    data object None : UiState()

    data object Loading : UiState()

    class Error(val error: Throwable) : UiState()

    class Success(val organisations: List<Organisation>) : UiState()
}

internal fun RequestResult<List<Organisation>>.toUiState(): UiState {
    return when (this) {
        is RequestResult.Error -> UiState.Error(error)
        is RequestResult.InProgress -> UiState.Loading
        is RequestResult.Success -> UiState.Success(data)
    }
}