package ru.kabirov.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "network")
data class Network(
    @PrimaryKey
    @ColumnInfo(name = "ip") val ip: String,
    @ColumnInfo(name = "organisation_id") val organisationId: String,
)
