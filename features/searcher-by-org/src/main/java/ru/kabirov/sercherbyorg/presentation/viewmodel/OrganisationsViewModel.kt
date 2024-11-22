package ru.kabirov.sercherbyorg.presentation.viewmodel

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
import ru.kabirov.data.api.OrganisationRepository

@HiltViewModel(assistedFactory = OrganisationsViewModel.OrganisationsViewModelFactory::class)
class OrganisationsViewModel @AssistedInject internal constructor(
    organisationRepository: OrganisationRepository,
    @Assisted query: String,
) : ViewModel() {

    var state: StateFlow<UiState> = organisationRepository
        .getOrganisationsByName(query)
        .map {
            it.toUiState()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.None)

    @AssistedFactory
    interface OrganisationsViewModelFactory {
        fun create(query: String): OrganisationsViewModel
    }
}