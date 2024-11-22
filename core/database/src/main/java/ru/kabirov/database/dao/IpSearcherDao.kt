package ru.kabirov.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kabirov.database.models.CheckAllSubnetForOrganisation
import ru.kabirov.database.models.SubnetDbo
import ru.kabirov.database.models.OrganisationDbo
import ru.kabirov.database.models.OrganisationNameQuery

@Dao
interface IpSearcherDao {
    @Query("SELECT org_id FROM organisation_name_query WHERE name_query = :nameQuery")
    fun getOrganisationIdsByNameQuery(nameQuery: String): Flow<List<String>>

    @Query("SELECT * FROM subnet WHERE organisation_id = :orgId")
    suspend fun getSubnetsByOrgId(orgId: String): List<SubnetDbo>

    @Query("SELECT * FROM organisation WHERE name LIKE '%' || :query || '%'")
    suspend fun getOrganisationsByName(query: String): List<OrganisationDbo>

    @Query("SELECT organisation_id FROM subnet WHERE inet_num = :subnet")
    fun getOrganisationIdBySubnet(subnet: String): Flow<String>

    @Query("SELECT * FROM organisation WHERE id = :id")
    fun getOrganisationByIdFlow(id: String): Flow<OrganisationDbo?>

    @Query("SELECT * FROM organisation WHERE id = :id")
    suspend fun getOrganisationById(id: String): OrganisationDbo

    @Query("SELECT * FROM subnet")
    suspend fun getAllSubnets(): List<SubnetDbo>

    @Query("SELECT * FROM subnet")
    fun getAllSubnetsFlow(): Flow<List<SubnetDbo>>

    @Query("SELECT has_all_subnets FROM check_org WHERE org_id = :orgId")
    fun hasAllSubnetsByOrgId(orgId: String): Flow<Boolean>

    @Insert
    suspend fun insertSubnet(subnet: SubnetDbo)

    @Insert
    suspend fun insertSubnets(subnets: List<SubnetDbo>)

    @Insert
    suspend fun insertOrganisation(organisation: OrganisationDbo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrganisations(organisations: List<OrganisationDbo>)

    @Insert
    suspend fun insertQuery(organisationNameQuery: OrganisationNameQuery)

    @Insert
    suspend fun insertHasAllSubnet(checkAllSubnetForOrganisation: CheckAllSubnetForOrganisation)
}