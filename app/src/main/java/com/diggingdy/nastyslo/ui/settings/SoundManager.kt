package com.diggingdy.nastyslo.ui.settings

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.annotation.RawRes

class SoundManager(private val context: Context) {

    private var soundPool: SoundPool
    private val soundIdMap = mutableMapOf<String, Int>()

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    fun loadSound(soundName: String, @RawRes resId: Int) {
        val soundId = soundPool.load(context, resId, 1)
        soundIdMap[soundName] = soundId
    }

    fun playSound(soundName: String, loop: Boolean = false) {
        val soundId = soundIdMap[soundName] ?: return

        if (loop) {
            val mediaPlayer = MediaPlayer.create(context, soundId)
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        } else {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun pause() {
        soundPool.autoPause()
    }

    fun resume() {
        soundPool.autoResume()
    }

    fun stopSound(soundName: String) {
        val soundId = soundIdMap[soundName] ?: return
        soundPool.stop(soundId)
    }

    fun release() {
        soundPool.release()
    }
}