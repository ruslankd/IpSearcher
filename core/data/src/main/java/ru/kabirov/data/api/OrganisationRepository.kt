package ru.kabirov.data.api

import kotlinx.coroutines.flow.Flow
import ru.kabirov.data.model.Subnet
import ru.kabirov.data.model.Organisation
import ru.kabirov.data.model.RequestResult

interface OrganisationRepository {
    fun getOrganisationsByName(name: String): Flow<RequestResult<List<Organisation>>>
    fun getSubnetsByOrgId(orgId: String): Flow<RequestResult<List<Subnet>>>
}