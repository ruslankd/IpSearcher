package ru.kabirov.organisation_info.presentation.viewmodel

import ru.kabirov.data.model.Organisation
import ru.kabirov.data.model.Subnet

data class StateData(
    val organisation: Organisation,
    val subnets: List<Subnet>,
)