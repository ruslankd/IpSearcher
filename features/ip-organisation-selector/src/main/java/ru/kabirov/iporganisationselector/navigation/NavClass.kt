package ru.kabirov.iporganisationselector.navigation

import kotlinx.serialization.Serializable

sealed class NavClass {

    @Serializable
    data object Empty : NavClass()

    @Serializable
    data class IpAddresses(val query: String) : NavClass()
}