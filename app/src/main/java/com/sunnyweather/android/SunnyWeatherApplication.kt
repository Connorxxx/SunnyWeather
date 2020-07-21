package com.sunnyweather.android

import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {

    companion object {
        const val TOKEN = ""   //input yourself Token
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
