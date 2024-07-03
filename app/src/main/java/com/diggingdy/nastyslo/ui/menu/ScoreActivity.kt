package com.diggingdy.nastyslo.ui.menu

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.diggingdy.nastyslo.HideNavigation
import com.diggingdy.nastyslo.R
import com.diggingdy.nastyslo.databinding.ActivityScoreBinding
import com.diggingdy.nastyslo.ui.settings.SettingsActivity
import com.diggingdy.nastyslo.ui.settings.SoundManager
import com.diggingdy.nastyslo.ui.settings.VibrateManager

class ScoreActivity : AppCompatActivity() {
    private val binding by lazy { ActivityScoreBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    private lateinit var soundManager: SoundManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideNavigation.hideNavigation(this)
        soundManager = SoundManager(this)
        soundMode()
        confirmGameSettingsButton()
    }

    private fun confirmGameSettingsButton() {
        var animation = AnimationUtils.loadAnimation(this, R.anim.scale)
        sharedPref = getSharedPreferences("diggingDynastyPref", MODE_PRIVATE)
        binding.btnOk.setOnClickListener {
            it.startAnimation(animation)
            vibrationMode()
            startActivity(Intent(this@ScoreActivity, SettingsActivity::class.java))
            finish()
        }
        binding.btnBack.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            vibrationMode()
            sharedPref.edit().putString("levelGame", "").apply()
            startActivity(Intent(this@ScoreActivity, LevelActivity::class.java))
            finish()
        }
    }

    private fun vibrationMode() {
        val isVibration = sharedPref.getBoolean("vibration_enabled", false)
        if (isVibration) {
            VibrateManager.vibrateDevice(this, 200)
        }
    }

    private fun soundMode() {
        val isSound = sharedPref.getBoolean("sound_enabled", false)
        if (isSound) {
            soundManager.apply {
                loadSound("backgroundMenu", R.raw.sound_background_menu)
                playSound("backgroundMenu", true)
            }
        } else {
            soundManager.apply {
                stopSound("backgroundMenu")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }
}