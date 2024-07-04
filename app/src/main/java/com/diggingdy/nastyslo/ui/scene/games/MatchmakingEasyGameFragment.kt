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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchmakingEasyGameBinding.inflate(layoutInflater, container, false)

        sharedPref =
            requireActivity().getSharedPreferences(
                "diggingDynastyPref",
                AppCompatActivity.MODE_PRIVATE
            )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSelectedLevel()
        initRecyclerSceneGame()
        controlGameButton()
    }

    private fun setSelectedLevel() {
        val setLevel = sharedPref.getString("levelGame", "")
        binding.textSelectedLevel.text = setLevel
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
        val level = sharedPref.getString("levelGame", "Easy") ?: "Easy"
        adapter = CardAdapter(cardList, level)
        recyclerView = binding.sceneGame

        recyclerView.layoutManager = GridLayoutManager(context, spanCount)

        recyclerView.adapter = adapter
        addCardItems()

        adapter.onCardClick = { cardItem, position ->
            handleCardClick(cardItem, position)
        }
    }

    private fun getSpanCountForLevel(): Int {
        return when (sharedPref.getString("levelGame", "Easy") ?: "Easy") {
            "Easy" -> 3
            "Medium" -> 4
            "Hard" -> 5
            else -> 3
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addCardItems() {
        val level = sharedPref.getString("levelGame", "Easy") ?: "Easy"
        val numPairs = when (level) {
            "Easy" -> 4 // 4 пари для easy (8 карток)
            "Medium" -> 8 // 8 пар для medium (16 карток)
            "Hard" -> 10 // 10 пар для hard (20 карток)
            else -> 4
        }

        imageList.clear()
        imageList.addAll(getImagesForLevel(level))

        cardList.clear()
        for (i in 0 until numPairs) {
            val imageResId = imageList[i]
            cardList.add(Card(imageResId))
            cardList.add(Card(imageResId))
        }

        if (level == "Easy") {
            cardList.add(Card(imageList[0]))
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
        if (firstCard?.imageResId == secondCard?.imageResId) {
            firstCard?.isMatched = true
            secondCard?.isMatched = true

            val firstPos = cardList.indexOf(firstCard)
            val secondPos = cardList.indexOf(secondCard)
            adapter.notifyItemChanged(firstPos)
            adapter.notifyItemChanged(secondPos)

        } else {
            firstCard?.isFlipped = false
            secondCard?.isFlipped = false

            val firstPos = cardList.indexOf(firstCard)
            val secondPos = cardList.indexOf(secondCard)
            adapter.notifyItemChanged(firstPos)
            adapter.notifyItemChanged(secondPos)
        }

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
}