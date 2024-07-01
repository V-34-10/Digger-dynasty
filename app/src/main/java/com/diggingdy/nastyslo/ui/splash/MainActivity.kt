package com.diggingdy.nastyslo.ui.splash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.diggingdy.nastyslo.HideNavigation
import com.diggingdy.nastyslo.R
import com.diggingdy.nastyslo.ui.menu.MenuActivity
import com.diggingdy.nastyslo.ui.privacy.PrivacyActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HideNavigation.hideNavigation(this)
        startPrivacy()
    }

    private fun startPrivacy() {
        sharedPref = getSharedPreferences("diggingDynastyPref", MODE_PRIVATE)
        val flagPrivacy = sharedPref.getBoolean("statusPrivacy", false)

        if (!flagPrivacy) {
            sharedPref.edit().putBoolean("statusPrivacy", true).apply()
        }

        lifecycleScope.launch {
            delay(3000L)

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