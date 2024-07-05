package com.diggingdy.nastyslo.ui.privacy

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.diggingdy.nastyslo.HideNavigation
import com.diggingdy.nastyslo.R
import com.diggingdy.nastyslo.databinding.ActivityPrivacyBinding
import com.diggingdy.nastyslo.ui.menu.MenuActivity
import com.diggingdy.nastyslo.ui.settings.VibrateManager
import com.diggingdy.nastyslo.ui.splash.MainActivity

class PrivacyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPrivacyBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideNavigation.hideNavigation(this)
        sharedPref = getSharedPreferences("diggingDynastyPref", MODE_PRIVATE)
        acceptPrivacyButton()
    }

    private fun acceptPrivacyButton() {
        var animation = AnimationUtils.loadAnimation(this, R.anim.scale)
        binding.btnAccept.setOnClickListener {
            it.startAnimation(animation)
            vibrationMode()
            startActivity(Intent(this@PrivacyActivity, MenuActivity::class.java))
            finish()
        }
        binding.textPrivacyLink.setOnClickListener {
            this.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
                startActivity(intent)
            }
        }
        binding.btnBack.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            vibrationMode()
            startActivity(Intent(this@PrivacyActivity, MainActivity::class.java))
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