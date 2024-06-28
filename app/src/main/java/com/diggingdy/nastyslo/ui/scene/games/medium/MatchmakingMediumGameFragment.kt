package com.diggingdy.nastyslo.ui.scene.games.medium

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diggingdy.nastyslo.R

class MatchmakingMediumGameFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_matchmaking_medium_game, container, false)
    }
}