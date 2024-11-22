package ru.kabirov.data.mapper

import ru.kabirov.data.model.Organisation
import ru.kabirov.database.models.OrganisationDbo
import ru.kabirov.ripeapi.models.BaseDto
import ru.kabirov.ripeapi.models.Object

internal fun OrganisationDbo.toOrganisation(): Organisation {
    return Organisation(
        id = id,
        name = name,
        country = country,
    )
}

internal fun BaseDto.toOrganisationDbo(): OrganisationDbo? {
    val attributes = objects?.obj?.first()?.attributes?.attribute
    if (attributes.isNullOrEmpty()) return null
    val id = attributes.find { it.name == "org" }?.value ?: return null
    val name = attributes.findLast { it.name == "mnt-by" }?.value ?: return null
    val country = attributes.find { it.name == "country" }?.value
    return OrganisationDbo(
        id = id,
        name = name,
        country = country,
    )
}

internal fun BaseDto.toOrganisation(): Organisation? {
    val attributes = objects?.obj?.first()?.attributes?.attribute
    if (attributes.isNullOrEmpty()) return null
    val id = attributes.find { it.name == "org" }?.value ?: return null
    val name = attributes.findLast { it.name == "mnt-by" }?.value ?: return null
    val country = attributes.find { it.name == "country" }?.value
    return Organisation(
        id = id,
        name = name,
        country = country,
    )
}

internal fun BaseDto.toOrganisationList(): List<Organisation> {
    val objects = objects?.obj ?: emptyList()
    return objects.map { obj ->
        val attributes = obj.attributes?.attribute
        if (attributes.isNullOrEmpty()) return emptyList()
        val id = attributes.find { it.name == "organisation" }?.value ?: return emptyList()
        val name = attributes.findLast { it.name == "org-name" }?.value ?: return emptyList()
        val country = attributes.find { it.name == "country" }?.value
        Organisation(
            id = id,
            name = name,
            country = country,
        )
    }
}

internal fun BaseDto.toOrganisationDboList(): List<OrganisationDbo> {
    val objects = objects?.obj ?: emptyList()
    return objects.map { obj ->
        val attributes = obj.attributes?.attribute
        if (attributes.isNullOrEmpty()) return emptyList()
        val id = attributes.find { it.name == "organisation" }?.value ?: return emptyList()
        val name = attributes.findLast { it.name == "org-name" }?.value ?: return emptyList()
        val country = attributes.find { it.name == "country" }?.value
        OrganisationDbo(
            id = id,
            name = name,
            country = country,
        )
    }
}
