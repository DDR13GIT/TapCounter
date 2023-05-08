package com.ddroy.tapcounter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT


class MainActivity : AppCompatActivity() {
    private var count = 0
    private var flag = 0
    private lateinit var txt: TextView
    private lateinit var img: ImageView
//    val sound_switch_flag = intent.getStringExtra("sound_switch_flag")
//    val vibration_switch_flag = intent.getStringExtra("vibration_switch_flag")
//    val volumeBtn_switch_flag = intent.getStringExtra("volumeBtn_switch_flag")

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

//                if(sound_switch_flag?.equals(1) == true)
//                    play_onPressVibration()

            }
        }

        decreaseButton.setOnClickListener {
            if(flag == 1){
            count--
            updateCountText()
        }}

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

    private fun play_onPressVibration() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) { // Vibrator availability checking
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                ) // New vibrate method for API Level 26 or higher
            } else {
                vibrator.vibrate(500) // Vibrate method for below API Level 26
            }
        }
    }
}

//decrese button bug
//settings page added
//settings page to main page nav
//main page topapp bar added
//main page topapp bar settings icon added
//main page topapp bar settings icon click listener added

//switch state needs to be saved
//vibration function fix needed

