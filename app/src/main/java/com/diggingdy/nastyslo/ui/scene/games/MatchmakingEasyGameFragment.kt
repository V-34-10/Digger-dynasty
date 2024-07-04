package com.diggingdy.nastyslo.ui.scene.games

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.diggingdy.nastyslo.R
import com.diggingdy.nastyslo.databinding.FragmentMatchmakingEasyGameBinding
import com.diggingdy.nastyslo.ui.menu.LevelActivity
import com.diggingdy.nastyslo.ui.menu.ScoreActivity
import com.diggingdy.nastyslo.ui.settings.SoundManager

class MatchmakingEasyGameFragment : Fragment() {
    private lateinit var binding: FragmentMatchmakingEasyGameBinding
    private lateinit var sharedPref: SharedPreferences
    private var selectedLevel: String = ""
    private lateinit var soundManager: SoundManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchmakingEasyGameBinding.inflate(layoutInflater, container, false)

        soundManager = context?.let { SoundManager(it) }!!
        sharedPref =
            requireActivity().getSharedPreferences(
                "diggingDynastyPref",
                AppCompatActivity.MODE_PRIVATE
            )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        soundMode()
        setSelectedLevel()
        context?.let { ControllerGame.initRecyclerSceneGame(binding, it) }
        controlGameButton()
    }

    private fun setSelectedLevel() {
        selectedLevel = sharedPref.getString("levelGame", "").toString()
        binding.textSelectedLevel.text = selectedLevel
    }

    private fun controlGameButton() {
        var animation = AnimationUtils.loadAnimation(context, R.anim.scale)
        binding.btnStart.setOnClickListener {
            it.startAnimation(animation)
            ControllerGame.resetGame()
        }
        binding.btnChange.setOnClickListener {
            animation = AnimationUtils.loadAnimation(context, R.anim.scale)
            it.startAnimation(animation)
            startActivity(Intent(context, LevelActivity::class.java))
            activity?.finish()
        }
        binding.btnHighScore.setOnClickListener {
            animation = AnimationUtils.loadAnimation(context, R.anim.scale)
            it.startAnimation(animation)
            startActivity(Intent(context, ScoreActivity::class.java))
            activity?.finish()
        }
        binding.btnBack.setOnClickListener {
            animation = AnimationUtils.loadAnimation(context, R.anim.scale)
            it.startAnimation(animation)
            activity?.onBackPressed()
        }
    }

    private fun soundMode() {
        val isSound = sharedPref.getBoolean("sound_enabled", false)
        if (isSound) {
            soundManager.apply {
                playSound(R.raw.sound_lucky_lucy, true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }
}