package ru.kabirov.ripeapi.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Objects (

    @SerialName("object") var obj : ArrayList<Object> = arrayListOf()

)
