package ru.kabirov.organisation_info.domain

import kotlinx.coroutines.flow.Flow
import ru.kabirov.data.api.IpAddressRepository
import ru.kabirov.data.model.Organisation
import ru.kabirov.data.model.RequestResult
import javax.inject.Inject

class GetOrganisationUseCase @Inject constructor(
    private val ipAddressRepository: IpAddressRepository,
) {
    operator fun invoke(orgId: String): Flow<RequestResult<Organisation>> {
        return ipAddressRepository
            .getOrganisationById(orgId)
    }
}