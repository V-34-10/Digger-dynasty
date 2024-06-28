package com.diggingdy.nastyslo.ui.scene.games.easy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diggingdy.nastyslo.R

class MatchmakingEasyGameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_matchmaking_easy_game, container, false)
    }
}