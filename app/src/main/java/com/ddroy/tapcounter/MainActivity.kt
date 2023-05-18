@file:Suppress("DEPRECATION")

package com.ddroy.tapcounter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.ddroy.tapcounter.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var count = 0
    private var flag = 0
    private lateinit var txt: TextView
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        txt = findViewById(R.id.countTxt)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        count = sharedPreferences.getInt("count", 0)
        txt.text = count.toString()

        binding.imageView3.setOnClickListener {
            if (flag == 0) {
                count++
                txt.text = count.toString()
                textAnimation()
                checkAndPlayVibration()


            }
        }

        binding.decreaseCountBtn.setOnClickListener {
            if (flag == 0) {
                count--


                updateCountText()
            }
        }

        binding.resetBtn.setOnClickListener {
            count = 0


            updateCountText()
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
                    val milliseconds = 100L
                    val amplitude = VibrationEffect.DEFAULT_AMPLITUDE
                    val effect = VibrationEffect.createOneShot(milliseconds, amplitude)

                    vibrator.vibrate(effect)
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()
        // Save the count value when the app is closed
        sharedPreferences.edit().putInt("count", count).apply()
    }

//    fun playTapSound() {
//        // Get the sound resource
//        val soundResId = R.raw.tap_sound
//
//        // Create a MediaPlayer object
//        val mediaPlayer = MediaPlayer.create(this, soundResId)
//
//        // Play the sound
//        mediaPlayer.start()
//    }

}


