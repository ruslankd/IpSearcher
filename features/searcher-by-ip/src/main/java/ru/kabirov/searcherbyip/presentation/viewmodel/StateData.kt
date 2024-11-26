package ru.kabirov.searcherbyip.presentation.viewmodel

import ru.kabirov.data.model.Subnet

data class StateData(
    val subnet: Subnet,
    val flagUri: String?,
)