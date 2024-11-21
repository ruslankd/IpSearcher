package ru.kabirov.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kabirov.database.models.SubnetDbo
import ru.kabirov.database.models.OrganisationDbo

@Dao
interface IpSearcherDao {
    @Query("SELECT * FROM subnet WHERE organisation_id = :orgId")
    suspend fun getSubnetsByOrgId(orgId: String): List<SubnetDbo>

    @Query("SELECT * FROM organisation WHERE name LIKE '%' || :query || '%'")
    suspend fun getOrganisationsByName(query: String): List<OrganisationDbo>

    @Query("SELECT organisation_id FROM subnet WHERE inet_num = :subnet")
    fun getOrganisationIdBySubnet(subnet: String): Flow<String>

    @Query("SELECT * FROM organisation WHERE id = :id")
    fun getOrganisationById(id: String): Flow<OrganisationDbo?>

    @Query("SELECT * FROM subnet")
    suspend fun getAllSubnets(): List<SubnetDbo>

    @Query("SELECT * FROM subnet")
    fun getAllSubnetsFlow(): Flow<List<SubnetDbo>>

    @Insert
    suspend fun insertSubnet(subnet: SubnetDbo)

    @Insert
    suspend fun insertOrganisation(organisation: OrganisationDbo)
}