package com.ddroy.tapcounter

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ddroy.tapcounter.databinding.ActivityMainBinding
import com.ddroy.tapcounter.viewmodel.CounterViewModel
import com.ddroy.tapcounter.utils.SoundManager
import com.ddroy.tapcounter.utils.VibrationManager
import com.ddroy.tapcounter.utils.ScreenManager
import com.ddroy.tapcounter.utils.ThemeManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CounterViewModel
    private lateinit var soundManager: SoundManager
    private lateinit var vibrationManager: VibrationManager
    private lateinit var screenManager: ScreenManager

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            ThemeManager.applyTheme(this, ThemeManager.getSavedTheme(this))
            setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CounterViewModel::class.java]
        soundManager = SoundManager(this)
        vibrationManager = VibrationManager(this)
        screenManager = ScreenManager(this)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.apply {
            imageView3.setOnClickListener { viewModel.incrementCount() }
            decreaseCountBtn.setOnClickListener { viewModel.decrementCount() }
            resetBtn.setOnClickListener { viewModel.resetCount() }
            lockBtn.setOnClickListener { viewModel.toggleLock() }
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.settings -> {
                        startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.count.observe(this) { count ->
            binding.countTxt.text = count.toString()
            animateCountText()
        }

        viewModel.isLocked.observe(this) { isLocked ->
            binding.lockBtn.setIconResource(
                if (isLocked) R.drawable.ic_lock_closed
                else R.drawable.ic_lock_open
            )
        }

        viewModel.playSound.observe(this) { shouldPlay ->
            if (shouldPlay) soundManager.playSound()
        }

        viewModel.vibrate.observe(this) { shouldVibrate ->
            if (shouldVibrate) vibrationManager.vibrate()
        }
    }

    private fun animateCountText() {
        binding.countTxt.apply {
            scaleX = 0.7f
            scaleY = 0.7f
            animate().scaleX(1f).scaleY(1f).setDuration(500).start()
        }
    }

    override fun onResume() {
        super.onResume()
        screenManager.keepScreenOn(viewModel.isScreenOnEnabled())
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return when {
            viewModel.handleVolumeButton(event) -> true
            else -> super.dispatchKeyEvent(event)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }
}