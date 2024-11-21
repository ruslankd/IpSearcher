package ru.kabirov.searcherbyip.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kabirov.data.IpAddressRepository
import ru.kabirov.searcherbyip.presentation.UiState
import ru.kabirov.searcherbyip.presentation.toUiState
import javax.inject.Inject

@HiltViewModel(assistedFactory = SubnetViewModel.SubnetViewModelFactory::class)
class SubnetViewModel @AssistedInject constructor(
    ipAddressRepository: IpAddressRepository,
    @Assisted ipAddress: String,
) : ViewModel() {
    var state: StateFlow<UiState> = ipAddressRepository
        .getSubnetByIp(ipAddress)
        .map {
            it.toUiState()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.None)


    @AssistedFactory
    interface SubnetViewModelFactory {
        fun create(ipAddress: String): SubnetViewModel
    }
}