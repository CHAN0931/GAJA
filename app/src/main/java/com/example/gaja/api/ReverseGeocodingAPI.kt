package com.example.gaja.api

import android.location.Geocoder
import com.example.gaja.ui.SearchFragment
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ReverseGeocodingAPI {
    @GET("map-reversegeocode/v2/gc?output=json")
    suspend fun gc(
        @Header("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @Header("X-NCP-APIGW-API-KEY") clientSecret: String,
        @Query("coords") coord: String,
    ): ReverseGeocodingResponse
}

class Area(val name: String? = null)

class Region(val area0: Area? = null,
             val area1: Area? = null,
             val area2: Area? = null,
             val area3 : Area? = null,
             val area4: Area? = null,) {
    override fun toString() = listOfNotNull(area1, area2, area3, area4).mapNotNull { it.name }.filter { it.isNotEmpty() }.joinToString { " " }
}

class Geocode(val region: Region ?= null)

class ReverseGeocodingResponse(val results: List<Geocode> = emptyList()){
    override fun toString() = results.firstOrNull()?.region?.toString() ?: "No Address"
}