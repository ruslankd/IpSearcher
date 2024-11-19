package ru.kabirov.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "network_range",
    primaryKeys = ["organisation_id", "inet_num"],
)
data class NetworkRange(
    @ColumnInfo("organisation_id") val organisationId: String,
    @ColumnInfo("net_name") val netName: String,
    @ColumnInfo("inet_num") val inetNum: String,
    @ColumnInfo("country") val country: String? = null,
)
