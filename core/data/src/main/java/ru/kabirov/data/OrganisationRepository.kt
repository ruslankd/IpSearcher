package ru.kabirov.data

import kotlinx.coroutines.flow.Flow
import ru.kabirov.data.model.Subnet
import ru.kabirov.data.model.Organisation

interface OrganisationRepository {
    suspend fun getOrganisationsByName(name: String): Flow<RequestResult<List<Organisation>>>
    suspend fun getSubnetsByOrgId(orgId: String): Flow<RequestResult<List<Subnet>>>
}