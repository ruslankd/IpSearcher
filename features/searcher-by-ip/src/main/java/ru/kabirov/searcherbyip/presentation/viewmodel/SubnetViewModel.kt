package ru.kabirov.searcherbyip.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.kabirov.searcherbyip.domain.GetSubnetUseCase

@HiltViewModel(assistedFactory = SubnetViewModel.SubnetViewModelFactory::class)
class SubnetViewModel @AssistedInject constructor(
    getSubnetUseCase: GetSubnetUseCase,
    @Assisted ipAddress: String,
) : ViewModel() {
    var state: StateFlow<UiState> = getSubnetUseCase
        .invoke(ipAddress)
        .distinctUntilChanged()
        .map {
            it.toUiState()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.None)


    @AssistedFactory
    interface SubnetViewModelFactory {
        fun create(ipAddress: String): SubnetViewModel
    }
}