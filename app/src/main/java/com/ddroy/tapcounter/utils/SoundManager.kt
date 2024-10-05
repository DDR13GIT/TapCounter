package com.ddroy.tapcounter.utils

import android.content.Context
import android.media.MediaPlayer
import com.ddroy.tapcounter.R

class SoundManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.beep)
        }
        mediaPlayer?.start()
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}