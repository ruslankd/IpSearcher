package ru.kabirov.database.dao

import androidx.room.Dao
import androidx.room.Query
import ru.kabirov.database.models.NetworkRange
import ru.kabirov.database.models.Organisation

@Dao
interface IpSearcherDao {
    @Query("SELECT * FROM network_range WHERE organisation_id = :id")
    suspend fun getNetworksById(id: String): List<NetworkRange>

    @Query("SELECT * FROM organisation WHERE name LIKE '%' || :query || '%'")
    suspend fun getOrganisationsByQuery(query: String): List<Organisation>

    @Query("SELECT organisation_id FROM network WHERE ip = :ip")
    suspend fun getOrganisationIdByIp(ip: String): String
}