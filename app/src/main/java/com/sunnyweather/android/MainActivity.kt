package com.sunnyweather.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_place.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (SunnyWeatherApplication.TOKEN.isEmpty()) {
            Toast.makeText(this, "请在SunnyWeatherApplication中输入Token！", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onBackPressed() {
        if (searchPlaceEdit.text.isEmpty()) {
            super.onBackPressed()
        }
        searchPlaceEdit.text.clear()
    }
}