package com.ddroy.tapcounter.viewmodel

import com.ddroy.tapcounter.utils.VibrationManager
import android.app.Application
import android.content.SharedPreferences
import android.view.KeyEvent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.ddroy.tapcounter.sharedPreference.PreferenceKeys
import com.ddroy.tapcounter.utils.SoundManager
import androidx.core.content.edit
import com.ddroy.tapcounter.sharedPreference.AppSharedPref

class CounterViewModel(application: Application) : AndroidViewModel(application) {

    private val _count = MutableLiveData<Int>()
    val count: LiveData<Int> = _count

    private val _isLocked = MutableLiveData<Boolean>()
    val isLocked: LiveData<Boolean> = _isLocked

    private val _showConfetti = MutableLiveData<Boolean>()
    val showConfetti: LiveData<Boolean> = _showConfetti

    var confettiCount: Int = AppSharedPref.getConfettiCount(getApplication()).toIntOrNull() ?: 0

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    private var mediaPlayer: SoundManager = SoundManager(application)
    private val vibratorManager = VibrationManager(getApplication())


    init {
        _count.value = prefs.getInt(PREF_COUNT, 0)
        _isLocked.value = false
         mediaPlayer = SoundManager(application)
    }

    fun isEnableConfetti(): Boolean {
        return prefs.getBoolean(PreferenceKeys.PREF_LOOP_MODE_ENABLED, false)
    }

    fun incrementCount() {
        if (_isLocked.value == false) {
            _count.value = (_count.value ?: 0) + 1
            updatePreferences()
            if(isMusicPlayable()) mediaPlayer.playMusic()
            if(isVibrationPlayable()) vibratorManager.vibrate(200)
            val loopEnabled = prefs.getBoolean(PreferenceKeys.PREF_LOOP_MODE_ENABLED, false)
            if (loopEnabled) {
                val loopNumber = prefs.getString(PreferenceKeys.PREF_LOOP_NUMBER, "50")?.toIntOrNull() ?: 50
                if (_count.value!! % loopNumber == 0) {
                    confettiCount++
                    _showConfetti.value = true
                    AppSharedPref.setConfettiCount(getApplication(),confettiCount.toString())
                }
            }
        }
    }

    fun decrementCount() {
        if (_isLocked.value == false) {
            _count.value = (_count.value ?: 0) - 1
            updatePreferences()
            if(isMusicPlayable()) mediaPlayer.playMusic()
            if(isVibrationPlayable()) vibratorManager.vibrate(200)

        }
    }

    private fun isMusicPlayable() : Boolean{
        return prefs.getBoolean(PreferenceKeys.PREF_SOUND_ENABLED, false)
    }

    private fun isVibrationPlayable() : Boolean{
        return prefs.getBoolean(PreferenceKeys.PREF_VIBRATION_ENABLED, false)
    }


    fun resetCount() {
        if (_isLocked.value == false) {
            _count.value = 0
            updatePreferences()
        }
    }
    fun toggleLock() {
        _isLocked.value = !(_isLocked.value ?: false)
    }

    fun handleVolumeButton(event: KeyEvent): Boolean {
        if (event.action != KeyEvent.ACTION_DOWN) return false // Only process ACTION_DOWN events
        if (!prefs.getBoolean(PreferenceKeys.PREF_VOLUME_ENABLED, false)) return false

        when (event.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> incrementCount()
            KeyEvent.KEYCODE_VOLUME_DOWN -> decrementCount()
            else -> return false
        }
        return true
    }
    
    private fun updatePreferences() {
        prefs.edit() { putInt(PREF_COUNT, _count.value ?: 0) }
    }

    fun resetConfettiState() {
        _showConfetti.value = false
    }

    fun resetConfettiCount() {
        confettiCount = 0
        AppSharedPref.setConfettiCount(getApplication(),confettiCount.toString())
    }
    
   override fun onCleared() {
        mediaPlayer.release()
        super.onCleared()
    }

    companion object {
        private const val PREF_COUNT = "count"
    }
}