package ru.kabirov.data.mapper

import ru.kabirov.data.model.Subnet
import ru.kabirov.database.models.OrganisationDbo
import ru.kabirov.database.models.SubnetDbo
import ru.kabirov.ripeapi.models.BaseDto

internal fun SubnetDbo.toSubnet(): Subnet {
    return Subnet(
        orgId = organisationId,
        subnet = subnet,
        subnetName = subnetName,
        country = country,
    )
}

internal fun BaseDto.toSubnetDbo(): SubnetDbo? {
    val attributes = objects?.obj?.first()?.attributes?.attribute
    if (attributes.isNullOrEmpty()) return null
    val orgId = attributes.find { it.name == "org" }?.value
    val subnetName = attributes.find { it.name == "netname" }?.value ?: ""
    val subnet = attributes.find { it.name == "inetnum" }?.value ?: return null
    val country = attributes.find { it.name == "country" }?.value
    return SubnetDbo(
        organisationId = orgId,
        subnetName = subnetName,
        subnet = subnet,
        country = country,
    )
}

internal fun BaseDto.toSubnet(): Subnet? {
    val attributes = objects?.obj?.first()?.attributes?.attribute
    if (attributes.isNullOrEmpty()) return null
    val orgId = attributes.find { it.name == "org" }?.value
    val subnetName = attributes.find { it.name == "netname" }?.value ?: ""
    val subnet = attributes.find { it.name == "inetnum" }?.value ?: return null
    val country = attributes.find { it.name == "country" }?.value
    return Subnet(
        orgId = orgId,
        subnetName = subnetName,
        subnet = subnet,
        country = country,
    )
}

internal fun BaseDto.toSubnetDboList(): List<SubnetDbo> {
    val objects = objects?.obj ?: emptyList()
    return objects.map { obj ->
        val attributes = obj.attributes?.attribute
        if (attributes.isNullOrEmpty()) return emptyList()
        val subnet = attributes.find { it.name == "inetnum" }?.value ?: return emptyList()
        val subnetName = attributes.findLast { it.name == "netname" }?.value ?: return emptyList()
        val orgId = attributes.findLast { it.name == "org" }?.value ?: return emptyList()
        val country = attributes.find { it.name == "country" }?.value
        SubnetDbo(
            subnet = subnet,
            subnetName = subnetName,
            organisationId = orgId,
            country = country,
        )
    }
}

internal fun BaseDto.toSubnetList(): List<Subnet> {
    val objects = objects?.obj ?: emptyList()
    return objects.map { obj ->
        val attributes = obj.attributes?.attribute
        if (attributes.isNullOrEmpty()) return emptyList()
        val subnet = attributes.find { it.name == "inetnum" }?.value ?: return emptyList()
        val subnetName = attributes.findLast { it.name == "netname" }?.value ?: return emptyList()
        val orgId = attributes.findLast { it.name == "org" }?.value ?: return emptyList()
        val country = attributes.find { it.name == "country" }?.value
        Subnet(
            subnet = subnet,
            subnetName = subnetName,
            orgId = orgId,
            country = country,
        )
    }
}