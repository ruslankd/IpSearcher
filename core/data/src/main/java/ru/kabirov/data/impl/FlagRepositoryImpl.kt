package ru.kabirov.data.impl

import ru.kabirov.data.api.FlagRepository
import javax.inject.Inject

class FlagRepositoryImpl @Inject constructor() : FlagRepository {
    override fun getUri(country: String): String {
        return "https://flagsapi.com/${country}/flat/64.png"
    }
}