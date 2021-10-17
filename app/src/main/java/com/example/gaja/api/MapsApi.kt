package com.example.gaja.api

import com.naver.maps.geometry.LatLng
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object MapsApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://naveropenapi.apigw.ntruss.com")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val reverseGeocodingService = retrofit.create<ReverseGeocodingService>()
    private val directionsService = retrofit.create<DirectionsService>()

    private lateinit var clientId: String
    private lateinit var clientSecret: String

    fun init(clientId: String, clientSecret: String) {
        if (!this::clientId.isInitialized) {
            MapsApi.clientId = clientId
        }
        if (!this::clientSecret.isInitialized) {
            MapsApi.clientSecret = clientSecret
        }
    }

    private fun LatLng.toApiParameter() = "${longitude},${latitude}"

    suspend fun reverseGeocode(coord: LatLng) =
        reverseGeocodingService.gc(clientId, clientSecret, coord.toApiParameter())

    suspend fun directions(start: LatLng, goal: LatLng) =
        directionsService.driving(clientId, clientSecret, start.toApiParameter(), goal.toApiParameter())
}