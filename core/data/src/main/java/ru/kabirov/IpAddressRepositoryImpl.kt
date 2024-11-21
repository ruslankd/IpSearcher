package ru.kabirov

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import ru.kabirov.data.IpAddressRepository
import ru.kabirov.data.RequestResult
import ru.kabirov.data.map
import ru.kabirov.data.mapper.toOrganisation
import ru.kabirov.data.mapper.toOrganisationDbo
import ru.kabirov.data.mapper.toSubnet
import ru.kabirov.data.mapper.toSubnetDbo
import ru.kabirov.data.model.Organisation
import ru.kabirov.data.model.Subnet
import ru.kabirov.data.toRequestResult
import ru.kabirov.database.IpSearcherDatabase
import ru.kabirov.database.models.SubnetDbo
import ru.kabirov.ripeapi.RipeApi
import ru.kabirov.ripeapi.models.BaseDto
import javax.inject.Inject

class IpAddressRepositoryImpl @Inject constructor(
    private val api: RipeApi,
    private val database: IpSearcherDatabase,
) : IpAddressRepository {
    override fun getSubnetByIp(ipAddress: String): Flow<RequestResult<Subnet>> {
        val start = flowOf<RequestResult<Subnet>>(RequestResult.InProgress())
        val subnetsFlow = database.ipSearcherDao.getAllSubnetsFlow().map { subnets ->
            val cachedSubnet = findSubnetForIp(subnets = subnets, ip = ipAddress)
            if (cachedSubnet == null) {
                val apiRequest = api.baseDtoByIp(ipAddress = ipAddress)
                if (apiRequest.isSuccess) {
                    saveSubnetDtoToCache(apiRequest.getOrThrow())
                }
                apiRequest
                    .map {
                        it.toSubnet()!!
                    }
                    .toRequestResult()
            } else {
                RequestResult.Success(cachedSubnet.toSubnet())
            }
        }
        return merge(start, subnetsFlow)
    }

    private suspend fun saveSubnetDtoToCache(baseDto: BaseDto) {
        baseDto.toSubnetDbo()?.let {
            database.ipSearcherDao.insertSubnet(it)
        }
    }

    private fun ipToLong(ip: String): Long {
        val parts = ip.split(".")
        return (parts[0].toLong() shl 24) + (parts[1].toLong() shl 16) + (parts[2].toLong() shl 8) + parts[3].toLong()
    }

    private fun findSubnetForIp(subnets: List<SubnetDbo>, ip: String): SubnetDbo? {
        val ipLong = ipToLong(ip)

        for (subnet in subnets) {
            val (startIpLong, endIpLong) = subnet.subnet.split(" - ").map { ipToLong(it) }

            if (ipLong in startIpLong..endIpLong) {
                return subnet
            }
        }

        return null
    }

    private fun isIpInSubnet(ip: String, subnet: Subnet): Boolean {
        val ipLong = ipToLong(ip)

        val (startIpLong, endIpLong) = subnet.subnet.split(" - ").map { ipToLong(it) }

        return ipLong in startIpLong..endIpLong
    }

    override suspend fun getOrganisationBySubnet(subnet: String): Flow<RequestResult<Organisation>> {
        val cachedOrganisationId = database.ipSearcherDao.getOrganisationIdBySubnet(subnet)
        val cachedOrganisation = database.ipSearcherDao.getOrganisationById(cachedOrganisationId)
        return if (cachedOrganisation == null) {
            getOrganisationByIdFromServer(subnet)
        } else {
            flowOf(RequestResult.Success(cachedOrganisation.toOrganisation()))
        }
    }

    private fun getOrganisationByIdFromServer(id: String): Flow<RequestResult<Organisation>> {
        val apiRequest =
            flow { emit(api.baseDtoByIdOrganisation(idOrganisation = id)) }
                .onEach { result ->
                    if (result.isSuccess) saveOrganisationDtoToCache(result.getOrThrow())
                }
                .map { it.toRequestResult() }
        val start = flowOf<RequestResult<BaseDto>>(RequestResult.InProgress())
        return merge(start, apiRequest)
            .map { result ->
                result.map { it.toOrganisation()!! }
            }
    }

    private suspend fun saveOrganisationDtoToCache(baseDto: BaseDto) {
        baseDto.toOrganisationDbo()?.let {
            database.ipSearcherDao.insertOrganisation(it)
        }
    }

}