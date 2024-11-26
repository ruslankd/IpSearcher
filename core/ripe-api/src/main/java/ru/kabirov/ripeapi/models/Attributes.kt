package ru.kabirov.ripeapi.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Attributes(

    @SerialName("attribute") var attribute: ArrayList<Attribute> = arrayListOf()

)