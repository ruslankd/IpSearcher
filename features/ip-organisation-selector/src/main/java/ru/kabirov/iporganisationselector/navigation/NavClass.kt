package ru.kabirov.iporganisationselector.navigation

import kotlinx.serialization.Serializable

sealed class NavClass {

    @Serializable
    data object Empty : NavClass()

    @Serializable
    data class Organisation(val query: String) : NavClass()

    @Serializable
    data class Subnet(val ipAddress: String) : NavClass()

    @Serializable
    data class OrganisationInfo(val orgId: String) : NavClass()
}