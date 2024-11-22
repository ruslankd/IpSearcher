package ru.kabirov.organisation_info.domain

import kotlinx.coroutines.flow.Flow
import ru.kabirov.data.api.OrganisationRepository
import ru.kabirov.data.model.RequestResult
import ru.kabirov.data.model.Subnet
import javax.inject.Inject

class GetSubnetsUseCase @Inject constructor(
    private val organisationRepository: OrganisationRepository,
) {
    operator fun invoke(orgId: String): Flow<RequestResult<List<Subnet>>> {
        return organisationRepository
            .getSubnetsByOrgId(orgId)
    }
}