package ru.kabirov.ripeapi.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class TermsAndConditions(

    @SerialName("type") var type: String? = null,
    @SerialName("href") var href: String? = null

)
