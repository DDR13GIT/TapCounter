package com.ddroy.tapcounter.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.view.KeyEvent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.ddroy.tapcounter.sharedPreference.PreferenceKeys
import com.ddroy.tapcounter.utils.SoundManager

class CounterViewModel(application: Application) : AndroidViewModel(application) {

    private val _count = MutableLiveData<Int>()
    val count: LiveData<Int> = _count

    private val _isLocked = MutableLiveData<Boolean>()
    val isLocked: LiveData<Boolean> = _isLocked

    private val _playSound = MutableLiveData<Boolean>()
    val playSound: LiveData<Boolean> = _playSound

    private val _vibrate = MutableLiveData<Boolean>()
    val vibrate: LiveData<Boolean> = _vibrate

    private val _showConfetti = MutableLiveData<Boolean>()
    val showConfetti: LiveData<Boolean> = _showConfetti

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    private var mediaPlayer: SoundManager = SoundManager(application)


    init {
        _count.value = prefs.getInt(PREF_COUNT, 0)
        _isLocked.value = false
         mediaPlayer = SoundManager(application)
    }

    fun incrementCount() {
        if (_isLocked.value == false) {
            _count.value = (_count.value ?: 0) + 1
            updatePreferences()
            triggerEffects()
            if(isMusicPlayable()) mediaPlayer.playMusic()

            // Check for loop milestone
            val loopEnabled = prefs.getBoolean(PreferenceKeys.PREF_LOOP_MODE_ENABLED, false)
            if (loopEnabled) {
                val loopNumber = prefs.getString(PreferenceKeys.PREF_LOOP_NUMBER, "50")?.toIntOrNull() ?: 50
                if (_count.value!! % loopNumber == 0) {
                    _showConfetti.value = true
                }
            }
        }
    }

    fun decrementCount() {
        if (_isLocked.value == false) {
            _count.value = (_count.value ?: 0) - 1
            updatePreferences()
            triggerEffects()
            if(isMusicPlayable()) mediaPlayer.playMusic()
        }
    }

    private fun isMusicPlayable() : Boolean{
        return prefs.getBoolean(PreferenceKeys.PREF_SOUND_ENABLED, false)
    }

    fun resetCount() {
        if (_isLocked.value == false) {
            _count.value = 0
            updatePreferences()
            triggerEffects()
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

    fun isScreenOnEnabled(): Boolean = prefs.getBoolean(PreferenceKeys.PREF_SCREEN_ON, false)

    private fun updatePreferences() {
        prefs.edit().putInt(PREF_COUNT, _count.value ?: 0).apply()
    }

    private fun triggerEffects() {
        //i think we don't need this ~ now viewmodel can handle the music manager . So observer is not needed
        _playSound.value = prefs.getBoolean(PreferenceKeys.PREF_SOUND_ENABLED, false)
        _vibrate.value = prefs.getBoolean(PreferenceKeys.PREF_VIBRATION_ENABLED, false)
    }
   override fun onCleared() {
        mediaPlayer.release()
        super.onCleared()
    }

    companion object {
        private const val PREF_COUNT = "count"
    }
}