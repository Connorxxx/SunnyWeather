package com.sunnyweather.android.ui.about

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.net.toUri
import com.sunnyweather.android.R
import kotlinx.android.synthetic.main.about_card.*
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        card_github.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = ("https://github.com/wuyinghao1/SunnyWeather").toUri()
            startActivity(intent)
        }
        card_email.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = ("mailto:wyh2542367414d@gmail.com").toUri()
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}