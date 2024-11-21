package ru.kabirov

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import ru.kabirov.data.IpAddressRepository
import ru.kabirov.data.RequestResult
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

    override fun getOrganisationById(orgId: String): Flow<RequestResult<Organisation>> {
        val start = flowOf<RequestResult<Organisation>>(RequestResult.InProgress())
        val organisationFlow =
            database.ipSearcherDao.getOrganisationById(orgId).map { cachedOrganisation ->
                if (cachedOrganisation == null) {
                    val apiRequest = api.baseDtoByIdOrganisation(idOrganisation = orgId)
                    if (apiRequest.isSuccess) {
                        saveOrganisationDtoToCache(apiRequest.getOrThrow())
                    }
                    apiRequest
                        .map {
                            it.toOrganisation()!!
                        }
                        .toRequestResult()
                } else {
                    RequestResult.Success(cachedOrganisation.toOrganisation())
                }
            }

        return merge(start, organisationFlow)
    }

    private suspend fun saveOrganisationDtoToCache(baseDto: BaseDto) {
        baseDto.toOrganisationDbo()?.let {
            database.ipSearcherDao.insertOrganisation(it)
        }
    }

}