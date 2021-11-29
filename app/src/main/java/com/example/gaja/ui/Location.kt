package com.example.gaja.ui

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.example.gaja.R
import com.example.gaja.api.DirectionsResponse
import com.example.gaja.api.MapsApi
import com.example.gaja.db.DBHelper
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.*
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class location : Fragment(), OnMapReadyCallback {
    lateinit var dbHelper: DBHelper
    lateinit var database: SQLiteDatabase
    var Check_Distance = 1
    var TAG:String = "로그"
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.fragment_location) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.fragment_location, it).commit()
            }
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        dbHelper = DBHelper(requireContext(), "bookmarker.db", null, 1)
        database = dbHelper.writableDatabase
        return inflater.inflate(R.layout.fragment_location, container, false)
    }
    override fun onMapReady(naverMap: NaverMap) {
        var contentValues = ContentValues()
        Log.d(TAG, "MainActivity - onMapReady")
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true //My Location
        val polyline = PolylineOverlay().apply {
            width = resources.getDimensionPixelSize(R.dimen.line_width)
            color = ResourcesCompat.getColor(resources, R.color.line_color, context?.theme)
        }
        val path = PathOverlay().apply {
            width = resources.getDimensionPixelSize(R.dimen.line_width)
            color = ResourcesCompat.getColor(resources, R.color.line_color, context?.theme)
            outlineColor = ResourcesCompat.getColor(resources, R.color.outline_color, context?.theme)
            patternImage = OverlayImage.fromResource(R.drawable.path_pattern)
        }
        val marker = Marker().apply{ //Marker
            icon = MarkerIcons.LIGHTBLUE
            isHideCollidedSymbols = true
            setOnClickListener {
                val cameraUpdate = CameraUpdate.scrollTo(position).animate(CameraAnimation.Easing)
                naverMap.moveCamera(cameraUpdate)
                true
            }
        }
        val infoWindow = InfoWindow().apply { //location information
            adapter = object : InfoWindow.DefaultTextAdapter(requireContext()){
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    val summary = infoWindow.tag as? DirectionsResponse.Route.Summary ?: return ""
    return "${summary.distance}m / ${summary.duration / 1000 / 60}분"
                }
            }
        }
        fun showMarker(coord: LatLng, caption: String = "${coord.latitude}\n${coord.longitude}"){
            marker.apply {
                position = coord
                captionText = caption
                map = naverMap
            }
        }
        fun showOverlays(coord: LatLng, caption: String? = null){
            marker.apply{
                if(caption == null){
                    captionText = ""
                    lifecycleScope.launch {
                        captionText = MapsApi.reverseGeocode(coord).toString()
                    }
                } else {
                    captionText = caption
                }
            }
            path.map = null
            infoWindow.close()
            if(naverMap.locationOverlay.isVisible) {
                Toast.makeText(requireContext(), "목적지 검색을 시작합니다.", Toast.LENGTH_LONG).show()
                lifecycleScope.launch {
                    MapsApi.directions(naverMap.locationOverlay.position, coord).firstRoute?.let {
                        path.coords = it.coords
                        path.map = naverMap
                        infoWindow.tag = it.summary
                        infoWindow.invalidate()
                        infoWindow.open(marker)
                        delay(TimeUnit.SECONDS.toSeconds(3))
                        while (it.summary.distance > 10) {
                            path.coords = it.coords
                            path.map = naverMap
                            infoWindow.tag = it.summary
                            infoWindow.invalidate()
                            infoWindow.open(marker)
                            delay(TimeUnit.SECONDS.toSeconds(3))
                        }
                        Toast.makeText(requireContext(), "목적지 검색을 종료합니다.", Toast.LENGTH_LONG).show()
                    }
                }
            }
            contentValues.put("$caption", "$coord") //Location Information => DB
            database.insert("bookmark",null,contentValues)
        }
        naverMap.setOnSymbolClickListener { symbol	->
            showMarker(symbol.position,	symbol.caption)
            showOverlays(symbol.position, symbol.caption)
            true
        }
        naverMap.setOnMapLongClickListener { pointF, latLng ->
            showMarker(latLng, )
            showOverlays(latLng, ).run { }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "MainActivity - onRequestPermissionsResult")
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                Log.d(TAG, "MainActivity - onRequestPermissionsResult 권한 거부됨")
                naverMap.locationTrackingMode = LocationTrackingMode.None
            } else {
                Log.d(TAG, "MainActivity - onRequestPermissionsResult 권한 승인됨")
                naverMap.locationTrackingMode = LocationTrackingMode.Follow

            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

        companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
