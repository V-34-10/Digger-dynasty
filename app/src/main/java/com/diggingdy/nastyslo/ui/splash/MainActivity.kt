package com.diggingdy.nastyslo.ui.splash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.diggingdy.nastyslo.HideNavigation
import com.diggingdy.nastyslo.R
import com.diggingdy.nastyslo.databinding.ActivityMainBinding
import com.diggingdy.nastyslo.ui.menu.MenuActivity
import com.diggingdy.nastyslo.ui.privacy.PrivacyActivity
import com.diggingdy.nastyslo.ui.settings.SoundManager
import com.diggingdy.nastyslo.ui.settings.VibrateManager

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    private lateinit var soundManager: SoundManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideNavigation.hideNavigation(this)
        soundManager = SoundManager(this)
        soundMode()
        startPrivacy()
    }

    private fun startPrivacy() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale)
        sharedPref = getSharedPreferences("diggingDynastyPref", MODE_PRIVATE)
        val flagPrivacy = sharedPref.getBoolean("statusPrivacy", false)

        if (!flagPrivacy) {
            sharedPref.edit().putBoolean("statusPrivacy", true).apply()
        }

        binding.btnPlay.setOnClickListener {
            it.startAnimation(animation)
            vibrationMode()
            binding.btnPlay.isEnabled = false
            val startIntent = if (!flagPrivacy) {
                Intent(this@MainActivity, PrivacyActivity::class.java)
            } else {
                Intent(this@MainActivity, MenuActivity::class.java)
            }

            startActivity(startIntent)
            finish()
        }
    }

    private fun vibrationMode() {
        val isVibration = sharedPref.getBoolean("vibration_enabled", false)
        if (isVibration) {
            VibrateManager.vibrateDevice(this, 200)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
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