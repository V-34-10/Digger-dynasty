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
import com.diggingdy.nastyslo.ui.splash.MainActivity

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private val managerAudio by lazy { getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    private var defaultVolume: Int = 50
    private var isVibration: Boolean = false
    private var isSound: Boolean = false
    private lateinit var sharedPref: SharedPreferences
    private lateinit var soundManager: SoundManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideNavigation.hideNavigation(this)
        soundManager = SoundManager(this)
        sharedPref = getSharedPreferences("diggingDynastyPref", MODE_PRIVATE)
        setSelectedThemeAndLevel()
        setButtonBackgroundSoundAndVibration()
        changeSettingsButton()
    }

    private fun setButtonBackgroundSoundAndVibration() {
        if (sharedPref.getBoolean("vibration_enabled", false)) {
            binding.buttonOnVibration.setBackgroundResource(R.drawable.button_on_color)
            binding.buttonOffVibration.setBackgroundResource(R.drawable.button_off)
        } else {
            binding.buttonOffVibration.setBackgroundResource(R.drawable.button_off_color)
            binding.buttonOnVibration.setBackgroundResource(R.drawable.button_on)
        }

        if (sharedPref.getBoolean("sound_enabled", false)) {
            binding.buttonOnSounds.setBackgroundResource(R.drawable.button_on_color)
            binding.buttonOffSounds.setBackgroundResource(R.drawable.button_off)
        } else {
            binding.buttonOffSounds.setBackgroundResource(R.drawable.button_off_color)
            binding.buttonOnSounds.setBackgroundResource(R.drawable.button_on)
        }
    }

    private fun setSelectedThemeAndLevel() {
        val setTheme = sharedPref.getString("themeGame", "")
        binding.textSelectedTheme.text = setTheme
        val setLevel = sharedPref.getString("levelGame", "")
        binding.textSelectedLevel.text = setLevel
    }

    private fun changeSettingsButton() {
        var animation = AnimationUtils.loadAnimation(this, R.anim.scale)

        isVibration = sharedPref.getBoolean("vibration_enabled", false)
        isSound = sharedPref.getBoolean("sound_enabled", false)

        binding.buttonOnSounds.setOnClickListener {
            it.startAnimation(animation)
            setOnSound()
            sharedPref.edit().putBoolean("sound_enabled", true).apply()
            binding.buttonOnSounds.setBackgroundResource(R.drawable.button_on_color)
            binding.buttonOffSounds.setBackgroundResource(R.drawable.button_off)
            vibrationMode()
            soundMode()
        }
        binding.buttonOffSounds.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            setOffSound()
            sharedPref.edit().putBoolean("sound_enabled", false).apply()
            binding.buttonOffSounds.setBackgroundResource(R.drawable.button_off_color)
            binding.buttonOnSounds.setBackgroundResource(R.drawable.button_on)
            vibrationMode()
            soundMode()
        }
        binding.buttonOnVibration.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            sharedPref.edit().putBoolean("vibration_enabled", true).apply()
            binding.buttonOnVibration.setBackgroundResource(R.drawable.button_on_color)
            binding.buttonOffVibration.setBackgroundResource(R.drawable.button_off)
            vibrationMode()
        }
        binding.buttonOffVibration.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            cancelVibration(this)
            sharedPref.edit().putBoolean("vibration_enabled", false).apply()
            binding.buttonOffVibration.setBackgroundResource(R.drawable.button_off_color)
            binding.buttonOnVibration.setBackgroundResource(R.drawable.button_on)
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
            startActivity(Intent(this@SettingsActivity, SceneActivity::class.java))
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
        val isVibration = sharedPref.getBoolean("vibration_enabled", false)
        if (isVibration) {
            vibrateDevice(this, 200)
        }
    }

    private fun soundMode() {
        isSound = sharedPref.getBoolean("sound_enabled", false)
        if (isSound) {
            soundManager.apply {
                playSound(R.raw.sound_background_menu, true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }
}