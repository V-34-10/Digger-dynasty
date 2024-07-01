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

class ScoreActivity : AppCompatActivity() {
    private val binding by lazy { ActivityScoreBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideNavigation.hideNavigation(this)
        confirmGameSettingsButton()
    }

    private fun confirmGameSettingsButton() {
        var animation = AnimationUtils.loadAnimation(this, R.anim.scale)
        binding.btnOk.setOnClickListener {
            it.startAnimation(animation)
            startActivity(Intent(this@ScoreActivity, SettingsActivity::class.java))
            finish()
        }
        binding.btnBack.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            sharedPref.edit().putString("levelGame", "").apply()
            startActivity(Intent(this@ScoreActivity, LevelActivity::class.java))
            finish()
        }
    }
}