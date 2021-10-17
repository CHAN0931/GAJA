package com.example.gaja

import android.app.Application
import com.example.gaja.api.MapsApi

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapsApi.init(getString(R.string.naver_map_sdk_client_id), getString(R.string.naver_map_sdk_client_secret))
    }
}