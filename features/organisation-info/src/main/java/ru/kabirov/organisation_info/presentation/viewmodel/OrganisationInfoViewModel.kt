package ru.kabirov.organisation_info.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.kabirov.data.IpAddressRepository
import ru.kabirov.data.OrganisationRepository
import ru.kabirov.data.RequestResult

@HiltViewModel(assistedFactory = OrganisationInfoViewModel.OrganisationInfoViewModelFactory::class)
class OrganisationInfoViewModel @AssistedInject constructor(
    ipAddressRepository: IpAddressRepository,
    organisationRepository: OrganisationRepository,
    @Assisted orgId: String,
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    var state: StateFlow<UiState> = ipAddressRepository
        .getOrganisationById(orgId)
        .flatMapConcat { organisationResult ->
            when (organisationResult) {
                is RequestResult.Success -> {
                    organisationRepository.getSubnetsByOrgId(organisationResult.data.id)
                        .map { subnetsResult ->
                            when (subnetsResult) {
                                is RequestResult.Success -> {
                                    RequestResult.Success(
                                        StateData(
                                            organisationResult.data,
                                            subnetsResult.data
                                        )
                                    ).toUiState()
                                }

                                is RequestResult.Error -> {
                                    RequestResult.Error<StateData>(subnetsResult.error).toUiState()
                                }

                                is RequestResult.InProgress -> {
                                    RequestResult.InProgress<StateData>().toUiState()
                                }
                            }
                        }
                }

                is RequestResult.Error -> {
                    flowOf(RequestResult.Error<StateData>(organisationResult.error).toUiState())
                }

                else -> {
                    flowOf(RequestResult.InProgress<StateData>().toUiState())
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.None)

    @AssistedFactory
    interface OrganisationInfoViewModelFactory {
        fun create(orgId: String): OrganisationInfoViewModel
    }
}