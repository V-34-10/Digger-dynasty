package com.diggingdy.nastyslo.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.diggingdy.nastyslo.HideNavigation
import com.diggingdy.nastyslo.R
import com.diggingdy.nastyslo.databinding.ActivitySettingsBinding
import com.diggingdy.nastyslo.ui.menu.LevelActivity
import com.diggingdy.nastyslo.ui.menu.MenuActivity
import com.diggingdy.nastyslo.ui.menu.ScoreActivity
import com.diggingdy.nastyslo.ui.scene.SceneActivity
import com.diggingdy.nastyslo.ui.settings.VibrateManager.cancelVibration
import com.diggingdy.nastyslo.ui.settings.VibrateManager.vibrateDevice

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private val managerAudio by lazy { getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    private var defaultVolume: Int = 0
    private var isVibration: Boolean = false
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideNavigation.hideNavigation(this)
        setSelectedThemeAndLevel()
        changeSettingsButton()
    }

    private fun setSelectedThemeAndLevel() {
        val setTheme = sharedPref.getString("themeGame", "")
        binding.textSelectedTheme.text = setTheme
        val setLevel = sharedPref.getString("levelGame", "")
        binding.textSelectedLevel.text = setLevel
    }

    private fun changeSettingsButton() {
        var animation = AnimationUtils.loadAnimation(this, R.anim.scale)
        sharedPref = getSharedPreferences("diggingDynastyPref", MODE_PRIVATE)
        isVibration = sharedPref.getBoolean("vibration_enabled", false)
        binding.buttonOnSounds.setOnClickListener {
            it.startAnimation(animation)
            setOnSound()
            binding.buttonOnSounds.setImageResource(R.drawable.button_on_color)
            binding.buttonOffSounds.setImageResource(R.drawable.button_off)
            vibrationMode()
        }
        binding.buttonOffSounds.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            setOffSound()
            binding.buttonOffSounds.setImageResource(R.drawable.button_off_color)
            binding.buttonOnSounds.setImageResource(R.drawable.button_on)
            vibrationMode()
        }
        binding.buttonOnVibration.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            sharedPref.edit().putBoolean("vibration_enabled", true).apply()
            binding.buttonOnVibration.setImageResource(R.drawable.button_on_color)
            binding.buttonOffVibration.setImageResource(R.drawable.button_off)
            vibrationMode()
        }
        binding.buttonOffVibration.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            cancelVibration(this)
            sharedPref.edit().putBoolean("vibration_enabled", false).apply()
            binding.buttonOffVibration.setImageResource(R.drawable.button_off_color)
            binding.buttonOnVibration.setImageResource(R.drawable.button_on)
            vibrationMode()
        }
        binding.buttonChangeTheme.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            sharedPref.edit().putString("themeGame", "").apply()
            vibrationMode()
            startActivity(Intent(this@SettingsActivity, MenuActivity::class.java))
            finish()
        }
        binding.buttonChangeLevel.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            sharedPref.edit().putString("levelGame", "").apply()
            vibrationMode()
            startActivity(Intent(this@SettingsActivity, LevelActivity::class.java))
            finish()
        }
        binding.buttonContinue.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            vibrationMode()
            it.startAnimation(animation)
            startActivity(Intent(this@SettingsActivity, SceneActivity::class.java))
            finish()
        }
        binding.btnBack.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            vibrationMode()
            it.startAnimation(animation)
            startActivity(Intent(this@SettingsActivity, ScoreActivity::class.java))
            finish()
        }
    }

    private fun setOnSound() {
        managerAudio.setStreamVolume(AudioManager.STREAM_MUSIC, defaultVolume, 0)
    }

    private fun setOffSound() {
        defaultVolume = managerAudio.getStreamVolume(AudioManager.STREAM_MUSIC)
        managerAudio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
    }

    private fun vibrationMode() {
        if (isVibration) {
            vibrateDevice(this, 200)
        }
    }
}