package ru.kabirov.sercherbyorg.presentation.viewmodel

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
import ru.kabirov.sercherbyorg.domain.GetOrganisationsUseCase

@HiltViewModel(assistedFactory = OrganisationsViewModel.OrganisationsViewModelFactory::class)
class OrganisationsViewModel @AssistedInject internal constructor(
    getOrganisationsUseCase: GetOrganisationsUseCase,
    getFlagUriUseCase: GetFlagUriUseCase,
    private val errorHandler: ErrorHandler,
    val imageLoader: ImageLoader,
    @Assisted query: String,
) : ViewModel() {

    var state: StateFlow<UiState> = getOrganisationsUseCase
        .invoke(query)
        .distinctUntilChanged()
        .map { requestResult ->
            when (requestResult) {
                is RequestResult.Error -> {
                    val error = errorHandler.handleError(requestResult.error)
                    RequestResult.Error(
                        when (error) {
                            is ErrorHandler.NotFoundException -> Throwable("No matches found")
                            else -> error
                        }
                    )
                }

                is RequestResult.InProgress -> RequestResult.InProgress()
                is RequestResult.Success -> RequestResult.Success(data = requestResult.data.map {
                    OrganisationWithFlagUri(
                        id = it.id,
                        name = it.name,
                        flagUri = it.country?.let { countryCode ->
                            getFlagUriUseCase.invoke(
                                countryCode
                            )
                        }
                    )
                })
            }
        }
        .map {
            it.toUiState()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.None)

    @AssistedFactory
    interface OrganisationsViewModelFactory {
        fun create(query: String): OrganisationsViewModel
    }
}