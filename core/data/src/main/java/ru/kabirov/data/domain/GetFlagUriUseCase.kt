package ru.kabirov.data.domain

import ru.kabirov.data.api.FlagRepository
import javax.inject.Inject

class GetFlagUriUseCase @Inject constructor(
    private val flagRepository: FlagRepository,
) {
    operator fun invoke(country: String): String {
        return flagRepository
            .getUri(country)
    }
}