package ru.kabirov.data

import kotlinx.coroutines.flow.Flow
import ru.kabirov.data.model.Subnet
import ru.kabirov.data.model.Organisation

interface IpAddressRepository {
    fun getSubnetByIp(ipAddress: String): Flow<RequestResult<Subnet>>
    fun getOrganisationById(orgId: String): Flow<RequestResult<Organisation>>
}