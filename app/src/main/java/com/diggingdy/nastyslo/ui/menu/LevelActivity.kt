package com.diggingdy.nastyslo.ui.menu

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.diggingdy.nastyslo.HideNavigation
import com.diggingdy.nastyslo.R
import com.diggingdy.nastyslo.databinding.ActivityLevelBinding
import com.diggingdy.nastyslo.ui.settings.SoundManager
import com.diggingdy.nastyslo.ui.settings.VibrateManager

class LevelActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLevelBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sound: SoundManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideNavigation.hideNavigation(this)
        sound = SoundManager(this)
        sharedPref = getSharedPreferences("diggingDynastyPref", MODE_PRIVATE)
        soundMode()
        choiceLevelGameButton()
    }

    private fun choiceLevelGameButton() {
        var animation = AnimationUtils.loadAnimation(this, R.anim.scale_btn)


        binding.btnEasyLevel.setOnClickListener {
            it.startAnimation(animation)
            vibrationMode()
            binding.btnEasyLevel.setBackgroundResource(R.drawable.level_easy_selected)
            binding.btnMediumLevel.setBackgroundResource(R.drawable.level_medium)
            binding.btnHardLevel.setBackgroundResource(R.drawable.level_hard)
            sharedPref.edit().putString("levelGame", "Easy").apply()
        }
        binding.btnMediumLevel.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale_btn)
            it.startAnimation(animation)
            vibrationMode()
            binding.btnMediumLevel.setBackgroundResource(R.drawable.level_medium_selected)
            binding.btnEasyLevel.setBackgroundResource(R.drawable.level_easy)
            binding.btnHardLevel.setBackgroundResource(R.drawable.level_hard)
            sharedPref.edit().putString("levelGame", "Medium").apply()
        }
        binding.btnHardLevel.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale_btn)
            it.startAnimation(animation)
            vibrationMode()
            binding.btnHardLevel.setBackgroundResource(R.drawable.level_hard_selected)
            binding.btnEasyLevel.setBackgroundResource(R.drawable.level_easy)
            binding.btnMediumLevel.setBackgroundResource(R.drawable.level_medium)
            sharedPref.edit().putString("levelGame", "Hard").apply()
        }
        binding.btnContinue.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            vibrationMode()
            val setLevel = sharedPref.getString("levelGame", "")
            if (setLevel.equals("")) {
                Toast.makeText(this, R.string.error_choice_level, Toast.LENGTH_SHORT).show()
            } else {
                it.startAnimation(animation)
                startActivity(Intent(this@LevelActivity, ScoreActivity::class.java))
                finish()
            }
        }
        binding.btnBack.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            vibrationMode()
            sharedPref.edit().putString("themeGame", "").apply()
            startActivity(Intent(this@LevelActivity, MenuActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        sound.resume()
    }

    override fun onPause() {
        super.onPause()
        sound.pause()
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
            sound.apply {
                playSound(R.raw.sound_background_menu, true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sound.release()
    }
}