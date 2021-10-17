package com.example.gaja.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.gaja.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.overlay.PolylineOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons

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

        return inflater.inflate(R.layout.fragment_location, container, false)
    }
    override fun onMapReady(naverMap: NaverMap) {       //MapReady!!
        Log.d(TAG, "MainActivity - onMapReady")
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
        //Marker
        //overlay
        val polyline = PolylineOverlay().apply {
            width = resources.getDimensionPixelSize(R.dimen.line_width)
//            color = ResourcesCompat.getColor(resources, R.color.line_color,)
        }
        val path = PathOverlay().apply {  }
        val marker = Marker().apply{
            icon = MarkerIcons.LIGHTBLUE
            isHideCollidedSymbols = true
            setOnClickListener {
                val cameraUpdate = CameraUpdate.scrollTo(position).animate(CameraAnimation.Easing)
                naverMap.moveCamera(cameraUpdate)
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
            Toast.makeText(requireContext(), "클릭\n${coord.latitude}\n${coord.longitude}", Toast.LENGTH_SHORT).show()
        }
        naverMap.setOnSymbolClickListener { symbol	->
            showMarker(symbol.position,	symbol.caption)
            true
        }
        naverMap.setOnMapLongClickListener { point, coord ->
            showMarker(coord)
            //MAP DIRECTION EVENT
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
