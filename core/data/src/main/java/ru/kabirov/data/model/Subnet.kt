package ru.kabirov.data.model

data class Subnet(
    val orgId: String? = null,
    val subnet: String,
    val subnetName: String,
    val country: String? = null
)
