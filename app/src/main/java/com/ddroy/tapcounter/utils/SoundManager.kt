package com.ddroy.tapcounter.utils

import android.app.Application
import android.media.AudioAttributes
import android.media.SoundPool
import com.ddroy.tapcounter.R

class SoundManager(val application: Application) {

    private val soundPool: SoundPool
    private val soundId: Int

    init{
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

          soundPool = SoundPool.Builder()
            .setMaxStreams(5) // Allow for multiple overlapping sounds
            .setAudioAttributes(audioAttributes)
            .build()

        // Load the sound file into SoundPool
        soundId = soundPool.load(application, R.raw.beep, 1)
    }

   fun  playMusic(){
       soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
   }
    fun release(){
        soundPool.release()
    }
}