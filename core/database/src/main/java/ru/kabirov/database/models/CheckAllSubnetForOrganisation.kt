package ru.kabirov.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "check_org")
data class CheckAllSubnetForOrganisation(
    @PrimaryKey
    @ColumnInfo("org_id") val orgId: String,
    @ColumnInfo("has_all_subnets") val hasAllSubnets: Boolean = false,
)