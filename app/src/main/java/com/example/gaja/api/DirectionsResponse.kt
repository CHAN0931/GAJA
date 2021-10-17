package com.example.gaja.api

import com.naver.maps.geometry.LatLng
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class DirectionsResponse(
    val route: Map<String, List<Route>>,
) {
    @JsonClass(generateAdapter = true)
    class Route(
        val summary: Summary,
        val path: List<List<Double>>,
    ) {
        @JsonClass(generateAdapter = true)
        class Summary(
            val distance: Int,
            val duration: Long,
        )

        val coords
            get() = path.map { LatLng(it[1], it[0]) }
    }

    val firstRoute
        get() = route.asIterable().firstOrNull()?.value?.firstOrNull()
}
