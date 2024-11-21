package ru.kabirov.data

import kotlinx.coroutines.flow.Flow
import ru.kabirov.data.model.Subnet
import ru.kabirov.data.model.Organisation

interface IpAddressRepository {
    fun getSubnetByIp(ipAddress: String): Flow<RequestResult<Subnet>>
    suspend fun getOrganisationBySubnet(subnet: String): Flow<RequestResult<Organisation>>
}