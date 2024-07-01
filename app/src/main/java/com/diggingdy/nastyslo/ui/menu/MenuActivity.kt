package com.diggingdy.nastyslo.ui.menu

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.diggingdy.nastyslo.HideNavigation
import com.diggingdy.nastyslo.R
import com.diggingdy.nastyslo.databinding.ActivityMenuBinding
import com.diggingdy.nastyslo.ui.settings.VibrateManager
import com.diggingdy.nastyslo.ui.splash.MainActivity

class MenuActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMenuBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideNavigation.hideNavigation(this)
        choiceThemeGameButton()
    }

    private fun choiceThemeGameButton() {
        var animation = AnimationUtils.loadAnimation(this, R.anim.scale)
        sharedPref = getSharedPreferences("diggingDynastyPref", MODE_PRIVATE)

        binding.btnLuckyTheme.setOnClickListener {
            it.startAnimation(animation)
            vibrationMode()
            binding.btnLuckyTheme.setImageResource(R.drawable.theme_lucky_selected)
            sharedPref.edit().putString("themeGame", "Lucky Lucy").apply()
        }
        binding.btnThemeOldJack.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            vibrationMode()
            it.startAnimation(animation)
            binding.btnLuckyTheme.setImageResource(R.drawable.theme_old_jack_selected)
            sharedPref.edit().putString("themeGame", "Old-Timer Jack").apply()
        }
        binding.btnThemeMaria.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            vibrationMode()
            it.startAnimation(animation)
            binding.btnLuckyTheme.setImageResource(R.drawable.theme_maria_selected)
            sharedPref.edit().putString("themeGame", "Mystic Maria").apply()
        }
        binding.btnContinue.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            vibrationMode()
            val setTheme = sharedPref.getString("themeGame", "")
            if (setTheme.equals("")) {
                Toast.makeText(this, R.string.error_choice_theme, Toast.LENGTH_SHORT).show()
            } else {
                it.startAnimation(animation)
                startActivity(Intent(this@MenuActivity, LevelActivity::class.java))
                finish()
            }
        }
        binding.btnBack.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            vibrationMode()
            startActivity(Intent(this@MenuActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun vibrationMode() {
        val isVibration = sharedPref.getBoolean("vibration_enabled", false)
        if (isVibration) {
            VibrateManager.vibrateDevice(this, 200)
        }
    }
}