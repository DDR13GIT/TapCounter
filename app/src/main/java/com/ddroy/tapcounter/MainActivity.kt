@file:Suppress("DEPRECATION")

package com.ddroy.tapcounter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.KeyEvent
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.ddroy.tapcounter.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){

    private lateinit var sharedPreferences: SharedPreferences
    private var count = 0
    private var flag = 0
    private lateinit var txt: TextView
    private lateinit var binding: ActivityMainBinding
    private lateinit var audioManager: AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        txt = findViewById(R.id.countTxt)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        count = sharedPreferences.getInt("count", 0)
        txt.text = count.toString()

        binding.imageView3.setOnClickListener {
            if (flag == 0) {
                count++
                txt.text = count.toString()
                textAnimation()
                checkAndPlayVibration()
                checkAndPlaySound()
            }
        }

        binding.decreaseCountBtn.setOnClickListener {
            if (flag == 0) {
                count--
                updateCountText()
                checkAndPlayVibration()
                checkAndPlaySound()
            }
        }

        binding.resetBtn.setOnClickListener {
            count = 0
            updateCountText()
            checkAndPlayVibration()
            checkAndPlaySound()

        }

            binding.lockBtn.setOnClickListener {
            when (flag) {
                0 -> {
                    flag = 1
                    Toast.makeText(this@MainActivity, "Counter Locked", LENGTH_SHORT).show()
                }

                1 -> {
                    flag = 0
                    Toast.makeText(this@MainActivity, "Counter Unlocked", LENGTH_SHORT).show()
                }
            }
        }




        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        keepScreenOn(this)

    }

    private fun updateCountText() {
        val builder = StringBuilder()
        builder.append(count)
        txt.text = builder.toString()
        sharedPreferences.edit().putInt("count", count).apply()
        textAnimation()
    }

    private fun textAnimation() {
        // Create an ObjectAnimator to animate the scale of the TextView
        val scaleXAnimator = ObjectAnimator.ofFloat(txt, "scaleX", 0.7f, 1.0f)
        val scaleYAnimator = ObjectAnimator.ofFloat(txt, "scaleY", 0.7f, 1.0f)
        val animatorSet = AnimatorSet()
        animatorSet.duration = 500
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator)
        animatorSet.start()
    }


    private fun checkAndPlayVibration() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val vibrationEnabled  = prefs.getBoolean("vibration_switch_key", false)

        binding.apply {
            if (vibrationEnabled) {
                val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

                // Check if the device supports vibration
                if (vibrator.hasVibrator()) {
                    val milliseconds = 50L
                    val amplitude = VibrationEffect.DEFAULT_AMPLITUDE
                    val effect = VibrationEffect.createOneShot(milliseconds, amplitude)

                    Handler().postDelayed({
                        vibrator.vibrate(effect)
                    }, 50)
                }
            }
        }

    }

    private fun checkAndPlaySound() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val soundEnabled  = prefs.getBoolean("sound_switch_key", false)

        if (soundEnabled) {
        // Get the sound resource
        val soundResId = R.raw.beep

        // Create a MediaPlayer object
        val mediaPlayer = MediaPlayer.create(this, soundResId)

        // Play the sound
            Handler().postDelayed({
                // Play the sound
                mediaPlayer.start()
            }, 50)
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val keyCode = event.keyCode

        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    handleVolumeUp()
                    return true
                }
                false
            }

            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    handleVolumeDown()
                    return true
                }
                false
            }

            else -> super.dispatchKeyEvent(event)
        }
    }
    private fun handleVolumeUp() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val volumeEnabled = prefs.getBoolean("volume_switch_key", false)

        if (volumeEnabled) {
            count++
            updateCountText()
            checkAndPlayVibration()
            checkAndPlaySound()

            // Adjust volume without triggering system sound controller
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
        }
    }

    private fun handleVolumeDown() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val volumeEnabled = prefs.getBoolean("volume_switch_key", false)

        if (volumeEnabled) {
            count--
            updateCountText()
            checkAndPlayVibration()
            checkAndPlaySound()

            // Prevent volume change by setting the volume to the current value
            val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0)
        }
    }

    private fun keepScreenOn(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val screenOnEnabled = prefs.getBoolean("screenOn_switch_key", false)


        // If the screen_on_key is true, keep the screen on
        if (screenOnEnabled) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ON_AFTER_RELEASE, "MyApp:KeepScreenOnTag")

            wakeLock.acquire(10*60*1000L /*10 minutes*/)
        }
    }

    override fun onResume() {
        super.onResume()
        // Restore the count value from SharedPreferences
        count = sharedPreferences.getInt("count", 0)
        txt.text = count.toString()

//        applyTheme()
//        recreate()
    }

    override fun onPause() {
        super.onPause()
        // Save the count value to SharedPreferences
        sharedPreferences.edit().putInt("count", count).apply()
    }


//    private fun applyTheme() {
//        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
//        val themePreference = prefs.getString("theme_preference", "Light")
//
//        when (themePreference) {
//            "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
//    }
}


