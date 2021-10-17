package com.example.gaja.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DirectionsService {
    @GET("map-direction/v1/driving")
    suspend fun driving(
        @Header("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @Header("X-NCP-APIGW-API-KEY") clientSecret: String,
        @Query("start") start: String,
        @Query("goal") goal: String,
    ): DirectionsResponse
}