package ru.kabirov.sercherbyorg.presentation.viewmodel

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
import ru.kabirov.sercherbyorg.domain.GetOrganisationsUseCase

@HiltViewModel(assistedFactory = OrganisationsViewModel.OrganisationsViewModelFactory::class)
class OrganisationsViewModel @AssistedInject internal constructor(
    getOrganisationsUseCase: GetOrganisationsUseCase,
    @Assisted query: String,
) : ViewModel() {

    var state: StateFlow<UiState> = getOrganisationsUseCase
        .invoke(query)
        .distinctUntilChanged()
        .map {
            it.toUiState()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.None)

    @AssistedFactory
    interface OrganisationsViewModelFactory {
        fun create(query: String): OrganisationsViewModel
    }
}