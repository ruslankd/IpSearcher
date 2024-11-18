package ru.kabirov.ripeapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Link(

    @SerialName("type") var type: String? = null,
    @SerialName("href") var href: String? = null,

)
