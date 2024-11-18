package ru.kabirov.ripeapi.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Attribute(

    @SerialName("name") var name: String? = null,
    @SerialName("value") var value: String? = null

)