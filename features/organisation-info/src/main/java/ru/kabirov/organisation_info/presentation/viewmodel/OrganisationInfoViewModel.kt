package ru.kabirov.organisation_info.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.kabirov.data.IpAddressRepository

@HiltViewModel(assistedFactory = OrganisationInfoViewModel.OrganisationInfoViewModelFactory::class)
class OrganisationInfoViewModel @AssistedInject constructor(
    ipAddressRepository: IpAddressRepository,
    @Assisted orgId: String,
) : ViewModel() {
    var state: StateFlow<UiState> = ipAddressRepository
        .getOrganisationById(orgId)
        .map {
            it.toUiState()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.None)

    @AssistedFactory
    interface OrganisationInfoViewModelFactory {
        fun create(orgId: String): OrganisationInfoViewModel
    }
}