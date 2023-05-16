package com.ddroy.tapcounter

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.android.material.materialswitch.MaterialSwitch

class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    private val SOUND_SWITCH_KEY = "sound_switch"
    private val VIBRATION_SWITCH_KEY = "vibration_switch"
    private val VOLUME_BTN_SWITCH_KEY = "volumeBtn_switch"

    val soundSwitch = findViewById<MaterialSwitch>(R.id.sound_switch)
    val vibrationSwitch = findViewById<MaterialSwitch>(R.id.sound_switch)
    val volumeBtnSwitch = findViewById<MaterialSwitch>(R.id.sound_switch)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // Retrieve the saved state and set the radio buttons accordingly
        val soundSwitchState = sharedPreferences.getBoolean(SOUND_SWITCH_KEY, false)
        soundSwitch.isChecked = soundSwitchState

        val vibrationSwitchState = sharedPreferences.getBoolean(VIBRATION_SWITCH_KEY, false)
        vibrationSwitch.isChecked = vibrationSwitchState

        val volumeBtnSwitchState = sharedPreferences.getBoolean(VOLUME_BTN_SWITCH_KEY, false)
        volumeBtnSwitch.isChecked = volumeBtnSwitchState

        // Set listeners for radio button changes
        soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean(SOUND_SWITCH_KEY, isChecked).apply()
        }

        vibrationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean(VIBRATION_SWITCH_KEY, isChecked).apply()
        }

        volumeBtnSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean(VOLUME_BTN_SWITCH_KEY, isChecked).apply()
        }


        val topAppBar =
            findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.settingsToolbar)
        topAppBar.setNavigationOnClickListener {
            finish()
        }


    }
}


