package com.csd051.superiora.ui.setting

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.csd051.superiora.R
import com.csd051.superiora.notification.NotificationWorker
import com.csd051.superiora.utils.DarkMode
import com.csd051.superiora.utils.NOTIFICATION_CHANNEL_ID
import com.csd051.superiora.utils.NOTIFICATION_CHANNEL_NAME
import java.util.*
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.settings)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private lateinit var workManager : WorkManager

        private lateinit var periodicWorkRequest : PeriodicWorkRequest
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val listPreference = findPreference<ListPreference>(getString(R.string.pref_key_dark))
            listPreference?.setOnPreferenceChangeListener{ _ , newValue ->
                when (newValue) {
                    getString(R.string.pref_dark_on) -> updateTheme(DarkMode.ON.value)
                    getString(R.string.pref_dark_off) -> updateTheme(DarkMode.OFF.value)
                    else -> updateTheme(DarkMode.FOLLOW_SYSTEM.value)
                }
                true
            }


            val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            prefNotification?.setOnPreferenceChangeListener { _, newValue ->
                val channelName = NOTIFICATION_CHANNEL_NAME
                workManager = WorkManager.getInstance(requireActivity())
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, 6)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                var time = calendar.timeInMillis - System.currentTimeMillis()
                if (calendar.timeInMillis < System.currentTimeMillis()) {
                    time += 86400000 // Adding 1 day interval if the time was exceeded
                }
                val data = Data.Builder()
                    .putString(NOTIFICATION_CHANNEL_ID, channelName)
                    .build()
                periodicWorkRequest = PeriodicWorkRequest.Builder(NotificationWorker::class.java, 1, TimeUnit.DAYS)
                    .setInitialDelay(time, TimeUnit.MILLISECONDS)
                    .setInputData(data)
                    .build()
                val newVal = newValue as Boolean
                if (newVal) {
                    workManager.enqueue(periodicWorkRequest)
                } else {
                    workManager.cancelWorkById(periodicWorkRequest.id)
                }

                true
            }
        }

        private fun updateTheme(mode: Int): Boolean {
            AppCompatDelegate.setDefaultNightMode(mode)
            requireActivity().recreate()
            return true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}