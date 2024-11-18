package ru.kabirov.ripeapi.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Version(

    @SerialName("version") var version: String? = null,
    @SerialName("timestamp") var timestamp: String? = null,
    @SerialName("commit-id") var commitId: String? = null

)
