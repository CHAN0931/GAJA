package com.example.gaja.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gaja.R
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.serach_menu.*

class SearchFragment : AppCompatActivity() {
    private val fusedLocationSource = FusedLocationSource(this, 100)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.serach_menu)
        my_location_button.setOnClickListener {
        }
    }
}