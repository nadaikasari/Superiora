package com.csd051.superiora.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.csd051.superiora.R
import com.csd051.superiora.notification.DailyReminder
import com.csd051.superiora.ui.login.LoginActivity
import com.csd051.superiora.utils.DarkMode

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

            val logout = findPreference<Preference>(getString(R.string.pref_key_logout))
            logout?.setOnPreferenceClickListener {
                val signOutIntent = Intent(activity, LoginActivity::class.java)
                startActivity(signOutIntent)
                true
            }

            val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            prefNotification?.setOnPreferenceChangeListener { _, newValue ->
                if(newValue.equals(true)) {
                    DailyReminder().setDailyReminder(requireContext().applicationContext)
                } else {
                    DailyReminder().cancelAlarm(requireContext().applicationContext)
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