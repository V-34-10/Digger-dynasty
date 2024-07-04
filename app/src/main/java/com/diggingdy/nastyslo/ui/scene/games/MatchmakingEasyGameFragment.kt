package com.diggingdy.nastyslo.ui.scene.games

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diggingdy.nastyslo.R
import com.diggingdy.nastyslo.adapters.CardAdapter
import com.diggingdy.nastyslo.databinding.FragmentMatchmakingEasyGameBinding
import com.diggingdy.nastyslo.model.Card
import com.diggingdy.nastyslo.ui.menu.LevelActivity
import com.diggingdy.nastyslo.ui.menu.ScoreActivity
import com.diggingdy.nastyslo.ui.settings.SoundManager

class MatchmakingEasyGameFragment : Fragment() {
    private lateinit var binding: FragmentMatchmakingEasyGameBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardAdapter
    private val cardList = mutableListOf<Card>()
    private var firstCard: Card? = null
    private var secondCard: Card? = null
    private var isFlipping = false
    private val imageList = mutableListOf<Int>()
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
        initRecyclerSceneGame()
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
            resetGame()
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

    private fun initRecyclerSceneGame() {
        val spanCount = getSpanCountForLevel()
        adapter = CardAdapter(cardList, selectedLevel)
        recyclerView = binding.sceneGame

        recyclerView.layoutManager = GridLayoutManager(context, spanCount)

        recyclerView.adapter = adapter
        addCardItems()

        adapter.onCardClick = { cardItem, position ->
            handleCardClick(cardItem, position)
        }
    }

    private fun getSpanCountForLevel(): Int {
        return when (selectedLevel) {
            "Easy" -> 3
            "Medium" -> 4
            "Hard" -> 5
            else -> 3
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addCardItems() {
        val numPairs = when (selectedLevel) {
            "Easy" -> 4 // 4 пари для easy (8 карток)
            "Medium" -> 8 // 8 пар для medium (16 карток)
            "Hard" -> 10 // 10 пар для hard (20 карток)
            else -> 4
        }

        imageList.clear()
        imageList.addAll(getImagesForLevel(selectedLevel))

        cardList.clear()
        var position = 0
        for (i in 0 until numPairs) {
            val imageResId = imageList[i]
            cardList.add(Card(imageResId, position = position++))
            cardList.add(Card(imageResId, position = position++))
        }

        if (selectedLevel == "Easy") {
            cardList.add(Card(imageList[0], position = position++))
        }

        cardList.shuffle()
        adapter.notifyDataSetChanged()
    }

    private fun getImagesForLevel(level: String): List<Int> {
        val baseImages = listOf(
            R.drawable.key_lucky_a,
            R.drawable.key_lucky_b,
            R.drawable.key_lucky_c,
            R.drawable.key_lucky_d,
            R.drawable.key_lucky_e
        )

        return when (level) {
            "Easy" -> baseImages + baseImages.take(4) // 9 карток для Easy
            "Medium" -> baseImages + baseImages + baseImages.take(3) // 16 карток для Medium
            "Hard" -> baseImages + baseImages + baseImages + baseImages.take(2) // 20 карток для Hard
            else -> baseImages + baseImages.take(4) // 9 карток за замовчуванням
        }
    }

    private fun handleCardClick(cardItem: Card, position: Int) {
        if (isFlipping || cardItem.isFlipped || cardItem.isMatched) return

        cardItem.isFlipped = true
        cardItem.position = position
        adapter.notifyItemChanged(position)

        if (firstCard == null) {
            firstCard = cardItem
        } else {
            secondCard = cardItem
            isFlipping = true

            Handler(Looper.getMainLooper()).postDelayed({
                checkMatch()
            }, 1000)
        }
    }

    private fun checkMatch() {
        val firstPos = firstCard?.position ?: -1
        val secondPos = secondCard?.position ?: -1

        if (firstCard?.imageResId == secondCard?.imageResId) {
            firstCard?.isMatched = true
            secondCard?.isMatched = true
        } else {
            firstCard?.isFlipped = false
            secondCard?.isFlipped = false
        }

        adapter.notifyItemChanged(firstPos)
        adapter.notifyItemChanged(secondPos)
        firstCard = null
        secondCard = null
        isFlipping = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun resetGame() {
        firstCard = null
        secondCard = null
        isFlipping = false

        cardList.shuffle()

        cardList.forEach { card ->
            card.isFlipped = false
            card.isMatched = false
        }

        adapter.notifyDataSetChanged()
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