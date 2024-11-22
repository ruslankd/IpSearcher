package ru.kabirov.sercherbyorg.domain

import kotlinx.coroutines.flow.Flow
import ru.kabirov.data.api.OrganisationRepository
import ru.kabirov.data.model.Organisation
import ru.kabirov.data.model.RequestResult
import javax.inject.Inject

class GetOrganisationsUseCase @Inject constructor(
    private val organisationRepository: OrganisationRepository,
) {
    operator fun invoke(name: String): Flow<RequestResult<List<Organisation>>> {
        return organisationRepository.getOrganisationsByName(name)
    }
}