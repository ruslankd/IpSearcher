package ru.kabirov.searcherbyip.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.kabirov.common.ErrorHandler
import ru.kabirov.data.domain.GetFlagUriUseCase
import ru.kabirov.data.model.RequestResult
import ru.kabirov.searcherbyip.domain.GetSubnetUseCase

@HiltViewModel(assistedFactory = SubnetViewModel.SubnetViewModelFactory::class)
class SubnetViewModel @AssistedInject constructor(
    getSubnetUseCase: GetSubnetUseCase,
    getFlagUriUseCase: GetFlagUriUseCase,
    val imageLoader: ImageLoader,
    private val errorHandler: ErrorHandler,
    @Assisted ipAddress: String,
) : ViewModel() {
    var state: StateFlow<UiState> = getSubnetUseCase
        .invoke(ipAddress)
        .distinctUntilChanged()
        .map { requestResult ->
            when (requestResult) {
                is RequestResult.Success -> {
                    RequestResult.Success(StateData(
                        subnet = requestResult.data,
                        flagUri = requestResult.data.country?.let { getFlagUriUseCase.invoke(it) }
                    ))
                }
                is RequestResult.Error -> RequestResult.Error(errorHandler.handleError(requestResult.error))
                is RequestResult.InProgress -> RequestResult.InProgress()
            }
        }
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.None)


    @AssistedFactory
    interface SubnetViewModelFactory {
        fun create(ipAddress: String): SubnetViewModel
    }
}