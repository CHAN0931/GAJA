package com.example.gaja.ui

import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gaja.R
import com.example.gaja.api.ReverseGeocodingAPI
import com.example.gaja.api.ReverseGeocodingResponse
import com.naver.maps.map.LocationSource
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.serach_menu.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class SearchFragment : AppCompatActivity() {
    private val fusedLocationSource = FusedLocationSource(this, 100)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.serach_menu)

        my_location_button.setOnClickListener {
        }
    }
}