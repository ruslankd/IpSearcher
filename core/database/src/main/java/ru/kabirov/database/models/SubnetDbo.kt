package ru.kabirov.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subnet")
data class SubnetDbo(
    @PrimaryKey
    @ColumnInfo("inet_num") val subnet: String,
    @ColumnInfo("net_name") val subnetName: String,
    @ColumnInfo("organisation_id") val organisationId: String? = null,
    @ColumnInfo("country") val country: String? = null,
)
