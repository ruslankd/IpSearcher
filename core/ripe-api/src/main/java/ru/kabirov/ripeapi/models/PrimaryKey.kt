package ru.kabirov.ripeapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrimaryKey(

    @SerialName("attribute") var attribute: ArrayList<Attribute> = arrayListOf()

)
