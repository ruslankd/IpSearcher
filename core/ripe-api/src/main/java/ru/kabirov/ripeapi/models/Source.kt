package ru.kabirov.ripeapi.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Source(

    @SerialName("id") var id: String? = null

)