package ru.kabirov.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import retrofit2.HttpException
import ru.kabirov.data.api.OrganisationRepository
import ru.kabirov.data.mapper.toOrganisation
import ru.kabirov.data.mapper.toOrganisationDboList
import ru.kabirov.data.mapper.toSubnet
import ru.kabirov.data.mapper.toSubnetDboList
import ru.kabirov.data.model.Organisation
import ru.kabirov.data.model.RequestResult
import ru.kabirov.data.model.Subnet
import ru.kabirov.database.IpSearcherDatabase
import ru.kabirov.database.models.CheckAllSubnetForOrganisation
import ru.kabirov.database.models.OrganisationNameQuery
import ru.kabirov.ripeapi.RipeApi
import ru.kabirov.ripeapi.models.BaseDto
import javax.inject.Inject

class OrganisationRepositoryImpl @Inject constructor(
    private val api: RipeApi,
    private val database: IpSearcherDatabase,
) : OrganisationRepository {
    override fun getOrganisationsByName(name: String): Flow<RequestResult<List<Organisation>>> {
        val start = flowOf<RequestResult<List<Organisation>>>(RequestResult.InProgress())
        val organisationFlow =
            database.ipSearcherDao.getOrganisationIdsByNameQuery(name).map { orgIds ->
                if (orgIds.isEmpty()) {
                    val apiRequest = api.baseDtoByNameOrganisation(name = name)
                    if (apiRequest.isSuccess) {
                        saveOrganisationsDtoToCache(apiRequest.getOrThrow(), name)
                        RequestResult.InProgress()
                    } else {
                        apiRequest.exceptionOrNull()?.let {
                            if (it is HttpException && it.code() == 404) {
                                database.ipSearcherDao.insertQueries(
                                    listOf(
                                        OrganisationNameQuery(
                                            nameQuery = name,
                                            orgId = "empty",
                                        )
                                    )
                                )
                                RequestResult.Success(emptyList())
                            } else {
                                RequestResult.Error(it)
                            }
                        } ?: RequestResult.Error(Throwable())
                    }
                } else if(orgIds.first() == "empty") {
                    RequestResult.Success(emptyList())
                } else {
                    RequestResult.Success(
                        orgIds.map { orgId ->
                            database.ipSearcherDao.getOrganisationById(orgId)
                        }
                            .map { it.toOrganisation() })
                }
            }
        return merge(start, organisationFlow)
    }

    private suspend fun saveOrganisationsDtoToCache(baseDto: BaseDto, name: String) {
        baseDto.toOrganisationDboList().let { orgs ->
            database.ipSearcherDao.insertOrganisations(orgs)
            database.ipSearcherDao.insertQueries(
                orgs.map {
                    OrganisationNameQuery(
                        nameQuery = name,
                        orgId = it.id,
                    )
                }.toList()
            )
        }
    }

    override fun getSubnetsByOrgId(orgId: String): Flow<RequestResult<List<Subnet>>> {
        val start = flowOf<RequestResult<List<Subnet>>>(RequestResult.InProgress())
        val subnetsFlow =
            database.ipSearcherDao.hasAllSubnetsByOrgId(orgId).map { hasAllSubnets ->
                if (hasAllSubnets) {
                    RequestResult.Success(
                        database.ipSearcherDao.getSubnetsByOrgId(orgId)
                            .map { it.toSubnet() })
                } else {
                    val apiRequest = api.baseDtoByIdOrganisation(idOrganisation = orgId)
                    if (apiRequest.isSuccess) {
                        saveSubnetsDtoToCache(apiRequest.getOrThrow(), orgId)
                        RequestResult.InProgress()
                    } else {
                        apiRequest.exceptionOrNull()?.let {
                            if (it is HttpException && it.code() == 404) {
                                database.ipSearcherDao.insertHasAllSubnet(
                                    CheckAllSubnetForOrganisation(
                                        orgId = orgId,
                                        true
                                    )
                                )
                            }
                        }
                        RequestResult.Error(apiRequest.exceptionOrNull() ?: Throwable())
                    }
                }
            }
        return merge(start, subnetsFlow)
    }

    private suspend fun saveSubnetsDtoToCache(baseDto: BaseDto, orgId: String) {
        baseDto.toSubnetDboList().let { subnets ->
            database.ipSearcherDao.insertSubnets(subnets)
            database.ipSearcherDao.insertHasAllSubnet(
                CheckAllSubnetForOrganisation(
                    orgId = orgId,
                    true
                )
            )
        }
    }
}