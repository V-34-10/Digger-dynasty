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

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideNavigation.hideNavigation(this)
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}