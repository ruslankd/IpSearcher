package ru.kabirov.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import ru.kabirov.data.mapper.toOrganisation
import ru.kabirov.data.mapper.toOrganisationDboList
import ru.kabirov.data.mapper.toOrganisationList
import ru.kabirov.data.mapper.toSubnet
import ru.kabirov.data.mapper.toSubnetDboList
import ru.kabirov.data.mapper.toSubnetList
import ru.kabirov.data.model.Organisation
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
                    }
                    apiRequest
                        .map { it.toOrganisationList() }
                        .toRequestResult()
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
            orgs.forEach {
                database.ipSearcherDao.insertQuery(
                    OrganisationNameQuery(
                        nameQuery = name,
                        orgId = it.id,
                    )
                )
            }
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
                    }
                    apiRequest
                        .map { it.toSubnetList() }
                        .toRequestResult()
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