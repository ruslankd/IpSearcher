package ru.kabirov.ripeapi.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Object(

    @SerialName("type") var type: String? = null,
    @SerialName("link") var link: Link? = Link(),
    @SerialName("source") var source: Source? = Source(),
    @SerialName("primary-key") var primaryKey: PrimaryKey? = PrimaryKey(),
    @SerialName("attributes") var attributes: Attributes? = Attributes()

)
