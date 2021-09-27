package com.example.gaja.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.gaja.R
import com.example.gaja.api.ReverseGeocodingAPI
import com.example.gaja.api.ReverseGeocodingResponse
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.overlay.PolylineOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.android.synthetic.main.fragment_location.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class location : Fragment(), OnMapReadyCallback {
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

        val retrofit = Retrofit.Builder()
            .baseUrl("https://naveropenapi.apigw.ntruss.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val reverseGeocodingService = retrofit.create<ReverseGeocodingAPI>()

        return inflater.inflate(R.layout.fragment_location, container, false)
    }
    override fun onMapReady(naverMap: NaverMap) {       //MapReady!!
        Log.d(TAG, "MainActivity - onMapReady")
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
        //Marker
        //overlay

        val polyline = PolylineOverlay().apply {  }
        val path = PathOverlay().apply {  }
        val marker = Marker().apply{
            icon = MarkerIcons.LIGHTBLUE
            isHideCollidedSymbols = true
            setOnClickListener {
                map = null
                true
            }
        }
        fun showMarker(coord: LatLng, caption: String = "${coord.latitude}\n${coord.longitude}"){
            marker.apply {
                position = coord
                captionText = caption
                map = naverMap
            }
        }
        //좌표->주소 변경필요
        fun showOverlays(coord: LatLng, caption: String? = null){
            marker.apply{
                if(caption == null){
                    captionText = ""
                }
            }
            if(naverMap.locationOverlay.isVisible){
                polyline.apply {
                    coords = listOf(naverMap.locationOverlay.position, coord)
                    map = naverMap
                }
                path.apply { coords = listOf(naverMap.locationOverlay.position, coord)
                map = naverMap
                }
            }
        }
        //MapClickEvent
        naverMap.setOnMapClickListener { point, coord ->
            Toast.makeText(requireContext(), "클릭\n${coord.latitude}\n${coord.longitude}", Toast.LENGTH_LONG).show()
        }

        naverMap.setOnMapLongClickListener { point, coord ->
            showMarker(coord)
            Toast.makeText(requireContext(), "핀마크 생성\n${coord.latitude}\n${coord.longitude}", Toast.LENGTH_LONG).show()
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
