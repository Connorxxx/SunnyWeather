package com.sunnyweather.android.ui

import android.content.Intent
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.ui.weather.WeatherActivity

object StartActivityIntent {
    fun startWeatherIntent(place: Place) =  Intent(
        SunnyWeatherApplication.context, WeatherActivity::class.java).apply {
        putExtra("location_lng", place.location.lng)
        putExtra("location_lat", place.location.lat)
        putExtra("place_name", place.name)
    }
}