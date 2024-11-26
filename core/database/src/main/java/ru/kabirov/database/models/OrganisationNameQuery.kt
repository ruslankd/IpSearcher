package ru.kabirov.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "organisation_name_query",
    primaryKeys = ["org_id", "name_query"],
)
data class OrganisationNameQuery(
    @ColumnInfo("org_id") val orgId: String,
    @ColumnInfo("name_query") val nameQuery: String,
)
