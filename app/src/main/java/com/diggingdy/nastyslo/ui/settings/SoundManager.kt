package com.diggingdy.nastyslo.ui.settings

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SoundManager(private val context: Context) {
    private val mediaPlayers = mutableMapOf<Int, MediaPlayer>()
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun playSound(@RawRes soundResId: Int, loop: Boolean = false) {
        if (mediaPlayers.containsKey(soundResId) && mediaPlayers[soundResId]?.isPlaying == true) {
            return
        }

        val mediaPlayer = MediaPlayer.create(context, soundResId).apply {
            isLooping = loop
            setOnCompletionListener {
                if (!loop) {
                    scope.launch {
                        releaseMediaPlayer(soundResId)
                    }
                }
            }
        }

        mediaPlayers[soundResId] = mediaPlayer
        mediaPlayer.start()
    }

    fun stopSound(@RawRes soundResId: Int) {
        scope.launch {
            releaseMediaPlayer(soundResId)
        }
    }

    fun release() {
        scope.cancel()
        mediaPlayers.values.forEach { it.release() }
        mediaPlayers.clear()
    }

    private fun releaseMediaPlayer(soundResId: Int) {
        mediaPlayers[soundResId]?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayers.remove(soundResId)
        }
    }
}