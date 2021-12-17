package com.csd051.superiora.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.csd051.superiora.R
import com.csd051.superiora.databinding.ActivityMainBinding
import com.csd051.superiora.ui.home.home.HomeActivity
import com.csd051.superiora.utils.DarkMode
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, TIME)

        val darkMode = PreferenceManager.getDefaultSharedPreferences(this).getString(resources.getString(
            R.string.pref_key_dark), null)
        darkMode?.let {
            val darkModeValue = it.uppercase(Locale.ROOT)
            val modeSelected =
                when(darkModeValue) {
                    DarkMode.ON.name -> DarkMode.ON
                    DarkMode.OFF.name -> DarkMode.OFF
                    else -> DarkMode.FOLLOW_SYSTEM
                }
            AppCompatDelegate.setDefaultNightMode(modeSelected.value)
        }
    }

    companion object {
        private const val TIME = 3000L
    }
}