package com.diggingdy.nastyslo.ui.menu

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.diggingdy.nastyslo.HideNavigation
import com.diggingdy.nastyslo.R
import com.diggingdy.nastyslo.databinding.ActivityScoreBinding
import com.diggingdy.nastyslo.model.LevelStats
import com.diggingdy.nastyslo.ui.scene.SceneActivity
import com.diggingdy.nastyslo.ui.scene.games.ControllerGame
import com.diggingdy.nastyslo.ui.scene.games.ControllerGame.loadStats
import com.diggingdy.nastyslo.ui.settings.SettingsActivity
import com.diggingdy.nastyslo.ui.settings.SoundManager
import com.diggingdy.nastyslo.ui.settings.VibrateManager

class ScoreActivity : AppCompatActivity() {
    private val binding by lazy { ActivityScoreBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sound: SoundManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideNavigation.hideNavigation(this)
        sound = SoundManager(this)
        sharedPref = getSharedPreferences("diggingDynastyPref", MODE_PRIVATE)
        soundMode()
        setHighScore()
        confirmGameSettingsButton()
    }

    @SuppressLint("SetTextI18n")
    private fun setHighScore() {
        loadStats(sharedPref)

        val easyStatsEasy = ControllerGame.stats["Easy"]!!

        binding.textWinRateEasy.text =
            getString(R.string.text_set_win_rate) + " %02d%%".format(calculateWinRate(easyStatsEasy).toInt())
        binding.textWinsEasy.text =
            getString(R.string.text_set_wins) + " " + easyStatsEasy.wins.toString()
        binding.textLosesEasy.text =
            getString(R.string.text_set_loses) + " " + easyStatsEasy.losses.toString()

        val easyStatsMedium = ControllerGame.stats["Medium"]!!

        binding.textWinRateMedium.text =
            getString(R.string.text_set_win_rate) + " %02d%%".format(
                calculateWinRate(
                    easyStatsMedium
                ).toInt()
            )
        binding.textWinsMedium.text =
            getString(R.string.text_set_wins) + " " + easyStatsMedium.wins.toString()
        binding.textLosesMedium.text =
            getString(R.string.text_set_loses) + " " + easyStatsMedium.losses.toString()

        val easyStatsHard = ControllerGame.stats["Hard"]!!

        binding.textWinRateHard.text =
            getString(R.string.text_set_win_rate) + " %02d%%".format(calculateWinRate(easyStatsHard).toInt())
        binding.textWinsHard.text =
            getString(R.string.text_set_wins) + " " + easyStatsHard.wins.toString()
        binding.textLosesHard.text =
            getString(R.string.text_set_loses) + " " + easyStatsHard.losses.toString()
    }

    private fun calculateWinRate(levelStats: LevelStats): Float {
        return if (levelStats.gamesPlayed > 0) {
            (levelStats.wins.toFloat() / levelStats.gamesPlayed) * 100
        } else {
            0f
        }
    }

    private fun confirmGameSettingsButton() {
        var animation = AnimationUtils.loadAnimation(this, R.anim.scale)

        binding.btnOk.setOnClickListener {
            it.startAnimation(animation)
            vibrationMode()
            startActivity(Intent(this@ScoreActivity, SettingsActivity::class.java))
            finish()
        }
        binding.btnBack.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            vibrationMode()
            startActivity(Intent(this@ScoreActivity, SceneActivity::class.java))
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