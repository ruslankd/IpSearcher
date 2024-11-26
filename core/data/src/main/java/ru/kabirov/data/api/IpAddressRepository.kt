package ru.kabirov.data.api

import kotlinx.coroutines.flow.Flow
import ru.kabirov.data.model.Subnet
import ru.kabirov.data.model.Organisation
import ru.kabirov.data.model.RequestResult

interface IpAddressRepository {
    fun getSubnetByIp(ipAddress: String): Flow<RequestResult<Subnet>>
    fun getOrganisationById(orgId: String): Flow<RequestResult<Organisation>>
}