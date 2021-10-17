package com.example.gaja.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ReverseGeocodingService {
    @GET("map-reversegeocode/v2/gc?output=json")
    suspend fun gc(
        @Header("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @Header("X-NCP-APIGW-API-KEY") clientSecret: String,
        @Query("coords") coord: String,
    ): ReverseGeocodingResponse
}