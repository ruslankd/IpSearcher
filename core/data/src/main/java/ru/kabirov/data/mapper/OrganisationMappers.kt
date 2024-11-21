package ru.kabirov.data.mapper

import ru.kabirov.data.model.Organisation
import ru.kabirov.database.models.OrganisationDbo
import ru.kabirov.ripeapi.models.BaseDto

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
    val id = attributes.find { it.name == "organisation" }?.value ?: return null
    val name = attributes.find { it.name == "org-name" }?.value ?: return null
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
    val id = attributes.find { it.name == "organisation" }?.value ?: return null
    val name = attributes.find { it.name == "org-name" }?.value ?: return null
    val country = attributes.find { it.name == "country" }?.value
    return Organisation(
        id = id,
        name = name,
        country = country,
    )
}
