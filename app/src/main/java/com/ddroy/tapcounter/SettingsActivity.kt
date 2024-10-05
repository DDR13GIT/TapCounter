package com.ddroy.tapcounter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import androidx.preference.ListPreference
import com.ddroy.tapcounter.databinding.SettingsActivityBinding
import com.ddroy.tapcounter.utils.PreferenceKeys

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rateAppBtn.setOnClickListener {
            rateApp()
        }
    }

    private fun rateApp() {
        val packageName = packageName
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        try {
            startActivity(intent)
        } catch (e: Exception) {
            // If Play Store app is not available, open the Play Store website
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            startActivity(webIntent)
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            setupVibrationPreference()
            setupSoundPreference()
            setupVolumeButtonPreference()
            setupScreenOnPreference()
            setupThemePreference()
        }

        private fun setupVibrationPreference() {
            findPreference<SwitchPreferenceCompat>(PreferenceKeys.PREF_VIBRATION_ENABLED)?.setOnPreferenceChangeListener { _, newValue ->
                // Handle vibration preference change if needed
                true
            }
        }

        private fun setupSoundPreference() {
            findPreference<SwitchPreferenceCompat>(PreferenceKeys.PREF_SOUND_ENABLED)?.setOnPreferenceChangeListener { _, newValue ->
                // Handle sound preference change if needed
                true
            }
        }

        private fun setupVolumeButtonPreference() {
            findPreference<SwitchPreferenceCompat>(PreferenceKeys.PREF_VOLUME_ENABLED)?.setOnPreferenceChangeListener { _, newValue ->
                // Handle volume button preference change if needed
                true
            }
        }

        private fun setupScreenOnPreference() {
            findPreference<SwitchPreferenceCompat>(PreferenceKeys.PREF_SCREEN_ON)?.setOnPreferenceChangeListener { _, newValue ->
                // Handle screen on preference change if needed
                true
            }
        }

        private fun setupThemePreference() {
            findPreference<ListPreference>(PreferenceKeys.PREF_THEME)?.setOnPreferenceChangeListener { _, newValue ->
                when (newValue) {
                    "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                true
            }
        }
    }
}