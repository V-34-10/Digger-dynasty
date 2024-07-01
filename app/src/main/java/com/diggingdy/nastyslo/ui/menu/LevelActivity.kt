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

class LevelActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLevelBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideNavigation.hideNavigation(this)
        choiceLevelGameButton()
    }

    private fun choiceLevelGameButton() {
        var animation = AnimationUtils.loadAnimation(this, R.anim.scale)
        sharedPref = getSharedPreferences("diggingDynastyPref", MODE_PRIVATE)

        binding.btnEasyLevel.setOnClickListener {
            it.startAnimation(animation)
            binding.btnEasyLevel.setImageResource(R.drawable.theme_lucky_selected)
            sharedPref.edit().putString("levelGame", "Easy").apply()
        }
        binding.btnMediumLevel.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            binding.btnMediumLevel.setImageResource(R.drawable.theme_old_jack_selected)
            sharedPref.edit().putString("levelGame", "Medium").apply()
        }
        binding.btnHardLevel.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
            it.startAnimation(animation)
            binding.btnHardLevel.setImageResource(R.drawable.theme_maria_selected)
            sharedPref.edit().putString("levelGame", "Hard").apply()
        }
        binding.btnContinue.setOnClickListener {
            animation = AnimationUtils.loadAnimation(this, R.anim.scale)
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
            sharedPref.edit().putString("themeGame", "").apply()
            startActivity(Intent(this@LevelActivity, MenuActivity::class.java))
            finish()
        }
    }
}