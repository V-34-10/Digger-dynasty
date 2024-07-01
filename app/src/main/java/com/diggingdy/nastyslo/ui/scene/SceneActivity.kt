package com.diggingdy.nastyslo.ui.scene

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.diggingdy.nastyslo.HideNavigation
import com.diggingdy.nastyslo.R
import com.diggingdy.nastyslo.databinding.ActivitySceneBinding
import com.diggingdy.nastyslo.ui.scene.games.easy.MatchmakingEasyGameFragment
import com.diggingdy.nastyslo.ui.scene.games.hard.MatchmakingHardGameFragment
import com.diggingdy.nastyslo.ui.scene.games.medium.MatchmakingMediumGameFragment
import com.diggingdy.nastyslo.ui.settings.SettingsActivity

class SceneActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySceneBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideNavigation.hideNavigation(this)
        setGameFragment()
    }

    private fun setGameFragment() {
        val fragmentGame: Fragment = when (sharedPref.getString("themeGame", "") ?: "") {
            getString(R.string.name_lucky) -> {
                MatchmakingEasyGameFragment()
            }

            getString(R.string.name_old_jack) -> {
                MatchmakingMediumGameFragment()
            }

            getString(R.string.name_maria) -> {
                MatchmakingHardGameFragment()
            }

            else -> {
                MatchmakingEasyGameFragment()
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_games, fragmentGame)
            .commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            Intent(this@SceneActivity, SettingsActivity::class.java).also {
                startActivity(it)
                super.onBackPressed()
                finish()
            }
        }
    }
}