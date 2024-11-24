package ru.kabirov.data.api

interface FlagRepository {
    fun getUri(country: String): String
}