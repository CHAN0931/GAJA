package com.example.gaja.api

import com.naver.maps.geometry.LatLng
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DirectionsAPI {
    @GET("map-direction/v1/driving")
    suspend fun get(
        @Header("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @Header("X-NCP-APIGW-API-KEY") clientSecret: String,
        @Query("start") start: String,
        @Query("goal") goal: String,
    ): DirectionsResponse
}
class DirectionsResponse(val route: Map<String, List<Route>>){
    val firstRoute
        get() = route.asIterable().firstOrNull()?.value?.firstOrNull()
}

class Route(val summary: Summary, val path: List<List<Double>>){
    val coords
        get() = path.map{LatLng(it[1], it[0])}
}
class Summary(val distance: Int, val duration: Long)