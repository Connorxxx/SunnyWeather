package com.sunnyweather.android.ui.weather

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.activity_weather.toolbar1
import kotlinx.android.synthetic.main.card_forecast.*
import kotlinx.android.synthetic.main.card_life_index.*
import kotlinx.android.synthetic.main.card_now.*
import kotlinx.android.synthetic.main.fragment_place.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "WeatherActivity"
    }

    val viewModel by lazy {
        ViewModelProvider(this).get(WeatherViewModel::class.java)
    }
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setSupportActionBar(toolbar1)
//        supportActionBar?.let {
//            it.setDisplayHomeAsUpEnabled(true)
//            it.setHomeAsUpIndicator(R.drawable.ic_menu)
//        }
        init()
        initDrawerLayout()
    }

    private fun init() {
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer {
            val weather = it.getOrNull()
            swipeRefresh.isRefreshing = false
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            }
        })
        onOffsetChanged()
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
    }
    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        swipeRefresh.isRefreshing = true
    }

    private fun onOffsetChanged() {
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener {
                _, verticalOffset ->
            Log.d(TAG, "onOffsetChanged: $verticalOffset")
            swipeRefresh.isEnabled = verticalOffset == 0
        })
    }

    private fun showWeatherInfo(weather: Weather) {
        collapsingToolbarLt.title = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        //card_now布局的数据
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        currentTemp.text = currentTempText
        currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        cureentAQI.text = currentPM25Text
        app_bar_image.setImageResource(getSky(realtime.skycon).bg)
        //card_forecast布局的数据
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                forecastLayout, false)
            val dataInfo = view.findViewById<TextView>(R.id.dateInfo)
            val skyIcon = view.findViewById<ImageView>(R.id.skyIcon)
            val skyInfo = view.findViewById<TextView>(R.id.skyInfo)
            val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dataInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()}"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }
        //card_life_index布局的数据
        val lifeIndex = daily.lifeIndex
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
    }

    private fun initDrawerLayout() {
        drawer = drawerLayout
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar1,
        0,
        0)
        drawer.addDrawerListener(toggle)
        drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onDrawerOpened(drawerView: View) {
                if (searchPlaceEdit.text.isEmpty()) {
                    val manager = searchPlaceEdit.context.getSystemService(
                        Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    searchPlaceEdit.requestFocus()
                    manager.showSoftInput(searchPlaceEdit, 0)
                }
            }
        })
        toggle.syncState()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawers()
         else super.onBackPressed()
    }
}