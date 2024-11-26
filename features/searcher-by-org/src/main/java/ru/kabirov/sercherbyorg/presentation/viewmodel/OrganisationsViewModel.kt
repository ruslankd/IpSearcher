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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.kabirov.common.AppDispatchers
import ru.kabirov.common.ErrorHandler
import ru.kabirov.common.resource.ResourceManager
import ru.kabirov.data.domain.GetFlagUriUseCase
import ru.kabirov.data.model.RequestResult
import ru.kabirov.sercherbyorg.domain.GetOrganisationsUseCase

@HiltViewModel(assistedFactory = OrganisationsViewModel.OrganisationsViewModelFactory::class)
class OrganisationsViewModel @AssistedInject internal constructor(
    getOrganisationsUseCase: GetOrganisationsUseCase,
    getFlagUriUseCase: GetFlagUriUseCase,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager,
    dispatchers: AppDispatchers,
    val imageLoader: ImageLoader,
    @Assisted query: String,
) : ViewModel() {

    var state: StateFlow<UiState> = getOrganisationsUseCase
        .invoke(query)
        .distinctUntilChanged()
        .map { requestResult ->
            when (requestResult) {
                is RequestResult.Error -> {
                    RequestResult.Error(errorHandler.handleError(requestResult.error))
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
        .flowOn(dispatchers.io)
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.None)

    fun getResourceString(resId: Int): String {
        return resourceManager.getString(resId)
    }

    @AssistedFactory
    interface OrganisationsViewModelFactory {
        fun create(query: String): OrganisationsViewModel
    }
}