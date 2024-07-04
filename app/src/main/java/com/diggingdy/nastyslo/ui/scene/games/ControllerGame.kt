package com.diggingdy.nastyslo.ui.scene.games

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.diggingdy.nastyslo.R
import com.diggingdy.nastyslo.adapters.CardAdapter
import com.diggingdy.nastyslo.model.Card
import com.diggingdy.nastyslo.databinding.FragmentMatchmakingEasyGameBinding
import com.diggingdy.nastyslo.databinding.FragmentMatchmakingHardGameBinding
import com.diggingdy.nastyslo.databinding.FragmentMatchmakingMediumGameBinding

object ControllerGame {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardAdapter
    private val cardList = mutableListOf<Card>()
    private var firstCard: Card? = null
    private var secondCard: Card? = null
    private var isFlipping = false
    private val imageList = mutableListOf<Int>()
    private var selectedLevel: String = ""
    private var selectedTheme: String = ""
    private var stepsCount = 0
    fun initRecyclerSceneGame(binding: ViewBinding, context: Context) {
        sharedPref =
            context.getSharedPreferences("diggingDynastyPref", AppCompatActivity.MODE_PRIVATE)
        selectedLevel = sharedPref.getString("levelGame", "").toString()
        selectedTheme = sharedPref.getString("themeGame", "").toString()
        val spanCount = getSpanCountForLevel()
        adapter = CardAdapter(cardList, selectedLevel, selectedTheme)

        when (binding) {
            is FragmentMatchmakingEasyGameBinding -> {
                recyclerView = binding.sceneGame
            }

            is FragmentMatchmakingMediumGameBinding -> {
                recyclerView = binding.sceneGame
            }

            is FragmentMatchmakingHardGameBinding -> {
                recyclerView = binding.sceneGame
            }
        }

        recyclerView.layoutManager = GridLayoutManager(context, spanCount)

        recyclerView.adapter = adapter
        addCardItems()

        adapter.onCardClick = { cardItem, position ->
            handleCardClick(cardItem, position, binding)
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
        val baseImages = when (selectedTheme) {
            "Lucky Lucy" -> {
                listOf(
                    R.drawable.key_lucky_a,
                    R.drawable.key_lucky_b,
                    R.drawable.key_lucky_c,
                    R.drawable.key_lucky_d,
                    R.drawable.key_lucky_e
                )
            }

            "Old-Timer Jack" -> {
                listOf(
                    R.drawable.key_old_jack_a,
                    R.drawable.key_old_jack_b,
                    R.drawable.key_old_jack_c,
                    R.drawable.key_old_jack_d,
                    R.drawable.key_old_jack_e
                )
            }

            "Mystic Maria" -> {
                listOf(
                    R.drawable.key_maria_a,
                    R.drawable.key_maria_b,
                    R.drawable.key_maria_c,
                    R.drawable.key_maria_d
                )
            }

            else -> {
                listOf(
                    R.drawable.key_lucky_a,
                    R.drawable.key_lucky_b,
                    R.drawable.key_lucky_c,
                    R.drawable.key_lucky_d,
                    R.drawable.key_lucky_e
                )
            }
        }

        return when (level) {
            "Easy" -> baseImages + baseImages.take(4) // 9 карток для Easy
            "Medium" -> baseImages + baseImages + baseImages.take(3) // 16 карток для Medium
            "Hard" -> baseImages + baseImages + baseImages + baseImages.take(2) // 20 карток для Hard
            else -> baseImages + baseImages.take(4) // 9 карток за замовчуванням
        }
    }

    private fun handleCardClick(cardItem: Card, position: Int, binding: ViewBinding) {
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

            setStepsAttemptsCard(binding)
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
        stepsCount++
        adapter.notifyItemChanged(firstPos)
        adapter.notifyItemChanged(secondPos)
        firstCard = null
        secondCard = null
        isFlipping = false
    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetGame(binding: ViewBinding) {
        firstCard = null
        secondCard = null
        isFlipping = false

        cardList.shuffle()

        cardList.forEach { card ->
            card.isFlipped = false
            card.isMatched = false
        }

        adapter.notifyDataSetChanged()
        stepsCount = 0
        setStepsAttemptsCard(binding)
    }

    private fun setStepsAttemptsCard(binding: ViewBinding) {
        when (binding) {
            is FragmentMatchmakingEasyGameBinding -> {
                binding.textSteps.text = stepsCount.toString()
            }

            is FragmentMatchmakingMediumGameBinding -> {
                binding.textSteps.text = stepsCount.toString()
            }

            is FragmentMatchmakingHardGameBinding -> {
                binding.textSteps.text = stepsCount.toString()
            }
        }
    }
}