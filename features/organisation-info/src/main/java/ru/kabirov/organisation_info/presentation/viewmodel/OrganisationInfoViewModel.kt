package ru.kabirov.organisation_info.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.kabirov.common.AppDispatchers
import ru.kabirov.common.ErrorHandler
import ru.kabirov.common.resource.ResourceManager
import ru.kabirov.data.domain.GetFlagUriUseCase
import ru.kabirov.data.model.Organisation
import ru.kabirov.data.model.RequestResult
import ru.kabirov.organisation_info.domain.GetOrganisationUseCase
import ru.kabirov.organisation_info.domain.GetSubnetsUseCase

@HiltViewModel(assistedFactory = OrganisationInfoViewModel.OrganisationInfoViewModelFactory::class)
class OrganisationInfoViewModel @AssistedInject constructor(
    getOrganisationUseCase: GetOrganisationUseCase,
    private val getSubnetsUseCase: GetSubnetsUseCase,
    private val getFlagUriUseCase: GetFlagUriUseCase,
    val imageLoader: ImageLoader,
    dispatchers: AppDispatchers,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager,
    @Assisted orgId: String,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    var state: StateFlow<UiState> = getOrganisationUseCase.invoke(orgId)
        .distinctUntilChanged()
        .flatMapConcat { organisationResult ->
            when (organisationResult) {
                is RequestResult.Success -> {
                    getStateData(organisationResult.data).map { it.toUiState() }
                }

                is RequestResult.Error -> {
                    flowOf(
                        RequestResult.Error<StateData>(
                            errorHandler.handleError(
                                organisationResult.error
                            )
                        ).toUiState()
                    )
                }

                else -> {
                    flowOf(RequestResult.InProgress<StateData>().toUiState())
                }
            }
        }
        .flowOn(dispatchers.io)
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.None)


    private fun getStateData(
        org: Organisation
    ): Flow<RequestResult<StateData>> {
        return getSubnetsUseCase.invoke(org.id)
            .distinctUntilChanged()
            .map { subnetsResult ->
                when (subnetsResult) {
                    is RequestResult.Success -> {
                        RequestResult.Success(
                            StateData(
                                organisation = org,
                                subnets = subnetsResult.data,
                                flagUri = org.country?.let { getFlagUriUseCase.invoke(it) },
                            )
                        )
                    }

                    is RequestResult.Error -> {
                        RequestResult.Error(errorHandler.handleError(subnetsResult.error))
                    }

                    is RequestResult.InProgress -> {
                        RequestResult.InProgress()
                    }
                }
            }
    }

    fun getFlagUri(country: String?): String? {
        return country?.let {
            getFlagUriUseCase.invoke(country)
        }
    }

    fun getResourceString(resId: Int): String {
        return resourceManager.getString(resId)
    }

    @AssistedFactory
    interface OrganisationInfoViewModelFactory {
        fun create(orgId: String): OrganisationInfoViewModel
    }
}