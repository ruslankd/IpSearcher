package ru.kabirov.ripeapi

import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kabirov.ripeapi.models.BaseDto

interface RipeApi {
    @Headers("Accept: application/json")
    @GET("search")
    suspend fun baseDtoByIdOrganisation(
        @Query("inverse-attribute") inverseAttribute: String = "org",
        @Query("type-filter") typeFilter: String = "inetnum",
        @Query("source") source: String = "ripe",
        @Query("query-string") idOrganisation: String,
        @Query("flags") flags: String = "no-referenced",
    ): Result<BaseDto>

    @Headers("Accept: application/json")
    @GET("search")
    suspend fun baseDtoByNameOrganisation(
        @Query("type-filter") typeFilter: String = "organisation",
        @Query("source") source: String = "ripe",
        @Query("query-string") name: String,
        @Query("flags") flags: String = "no-referenced",
    ): Result<BaseDto>

    @Headers("Accept: application/json")
    @GET("search")
    suspend fun baseDtoByIp(
        @Query("type-filter") typeFilter: String = "inetnum",
        @Query("source") source: String = "ripe",
        @Query("query-string") ipAddress: String,
        @Query("flags") flags: String = "no-referenced",
    ): Result<BaseDto>

    @Headers("Accept: application/json")
    @GET("ripe/inetnum/{subnet}")
    suspend fun baseDtoBySubnet(
        @Path("inetnum") subnet: String
    ): Result<BaseDto>
}

fun RipeApi(
    baseUrl: String,
    json: Json = Json { ignoreUnknownKeys = true }
): RipeApi {
    return retrofit(baseUrl, json).create(RipeApi::class.java)
}

private fun retrofit(
    baseUrl: String,
    json: Json
): Retrofit {
    val jsonConverterFactory = json.asConverterFactory(MediaType.parse("application/json")!!)

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConverterFactory)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .build()
}