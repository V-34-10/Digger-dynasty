package com.diggingdy.nastyslo.model

data class Card(
    val imageResId: Int,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false,
    var position: Int = -1
)