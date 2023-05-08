package com.ddroy.tapcounter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.materialswitch.MaterialSwitch

class SettingsActivity : AppCompatActivity() {
    var sound_switch_flag = 0;
    var vibration_switch_flag = 0;
    var volumeBtn_switch_flag = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.settings, SettingsFragment())
//                .commit()
//        }
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val sound_switch = findViewById<MaterialSwitch>(R.id.sound_switch)
        val vibration_switch = findViewById<MaterialSwitch>(R.id.vibration_switch)
        val volumeBtn_switch = findViewById<MaterialSwitch>(R.id.volumeBtn_switch)

        sound_switch.setOnClickListener {
            if (sound_switch.isChecked) {
                sound_switch_flag = 0;
            } else {
                sound_switch_flag = 1;
            }
        }

        vibration_switch.setOnClickListener {
            if (vibration_switch.isChecked) {
                vibration_switch_flag = 0;
            } else {
                vibration_switch_flag = 1;
            }

        }

        volumeBtn_switch.setOnClickListener {
            if (volumeBtn_switch.isChecked) {
                volumeBtn_switch_flag = 0;
            } else {
                volumeBtn_switch_flag = 1;
            }
        }

        val topAppBar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.settingsToolbar)
       topAppBar.setNavigationOnClickListener {
           finish()
       }

//        val intent = Intent(this, MainActivity::class.java)
//        intent.putExtra("sound_switch_flag", sound_switch_flag)
//        intent.putExtra("vibration_switch_flag", vibration_switch_flag)
//        intent.putExtra("volumeBtn_switch_flag", volumeBtn_switch_flag)
//        startActivity(intent)
    }


//    class SettingsFragment : PreferenceFragmentCompat() {
//        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
//            setPreferencesFromResource(R.xml.root_preferences, rootKey)
//        }
//    }
}


