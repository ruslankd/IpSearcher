package ru.kabirov.data

import kotlinx.coroutines.flow.Flow
import ru.kabirov.data.model.Subnet
import ru.kabirov.data.model.Organisation

interface OrganisationRepository {
    fun getOrganisationsByName(name: String): Flow<RequestResult<List<Organisation>>>
    fun getSubnetsByOrgId(orgId: String): Flow<RequestResult<List<Subnet>>>
}