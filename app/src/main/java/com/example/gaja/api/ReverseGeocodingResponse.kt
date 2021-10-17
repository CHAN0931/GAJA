package com.example.gaja.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ReverseGeocodingResponse(
    val results: List<Geocode> = emptyList(),
) {
    @JsonClass(generateAdapter = true)
    class Geocode(
        val region: Region? = null,
    ) {
        @JsonClass(generateAdapter = true)
        class Region(
            val area0: Area? = null,
            val area1: Area? = null,
            val area2: Area? = null,
            val area3: Area? = null,
            val area4: Area? = null,
        ) {
            @JsonClass(generateAdapter = true)
            class Area(
                val name: String? = null,
            )

            override fun toString() = listOfNotNull(area1, area2, area3, area4)
                .mapNotNull { it.name }
                .filter { it.isNotEmpty() }
                .joinToString(" ")
        }
    }

    override fun toString() = results.firstOrNull()?.region?.toString() ?: "No Address"
}