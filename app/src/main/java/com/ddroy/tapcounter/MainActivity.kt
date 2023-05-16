package com.ddroy.tapcounter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.preference.PreferenceManager


class MainActivity : AppCompatActivity() {
    private var count = 0
    private var flag = 0
    private lateinit var txt: TextView
    private lateinit var img: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        val decreaseButton = findViewById<Button>(R.id.decreaseCountBtn)
        val resetButton = findViewById<Button>(R.id.resetBtn)
        val lockButton = findViewById<Button>(R.id.lockBtn)
        txt = findViewById(R.id.countTxt)
        img = findViewById(R.id.imageView3)

        img.setOnClickListener {
            if (flag == 0) {
                count++
                txt.text = count.toString()
                textAnimation()
                checkAndPlayVibration()
            }
        }

        decreaseButton.setOnClickListener {
            if (flag == 1) {
                count--
                updateCountText()
            }
        }

        resetButton.setOnClickListener {
            count = 0
            updateCountText()
        }

        lockButton.setOnClickListener {
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

        val topAppBar =
            findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)
//        topAppBar.setNavigationOnClickListener {
//            // Handle navigation icon press
//        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
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
        val vibrationSwitchState = sharedPreferences.getBoolean(VIBRATION_SWITCH_KEY, false)

        if (vibrationSwitchState) {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            // Check if the device supports vibration
            if (vibrator.hasVibrator()) {
                // Vibrate for 100 milliseconds
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
                    )
                } else {
                    val vibrationEffect = VibrationEffect.createOneShot(100, 50)
                    vibrator.vibrate(vibrationEffect)
                }
            }
        }
    }

}

// trying vibration on button click
// need to store the count value using shared preferences


