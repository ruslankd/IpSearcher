package ru.kabirov.ripeapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseDto(
    @SerialName("objects") var objects: Objects? = Objects(),
    @SerialName("terms-and-conditions") var termsAndConditions: TermsAndConditions? = TermsAndConditions(),
    @SerialName("version") var version: Version? = Version(),
)
