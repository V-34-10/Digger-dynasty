package com.diggingdy.nastyslo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.diggingdy.nastyslo.R
import com.diggingdy.nastyslo.model.Card

class CardAdapter(private val cardList: List<Card>, private val level: String) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    var onCardClick: ((Card, Int) -> Unit)? = null

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardImage: ImageView = itemView.findViewById(R.id.cardImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.card_image, parent, false)
        val cardImage: ImageView = itemView.findViewById(R.id.cardImage)

        val cardSize = if (level == "Medium" || level == "Hard") {
            80.dpToPx(parent.context) // 80dp для Medium та Hard
        } else {
            100.dpToPx(parent.context) // 60dp для Easy
        }
        cardImage.layoutParams.width = cardSize
        cardImage.layoutParams.height = cardSize

        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardList[position]
        if (cardItem.isFlipped) {
            holder.cardImage.setImageResource(cardItem.imageResId)
        } else {
            holder.cardImage.setImageResource(R.drawable.key_lucky_f)
        }

        holder.itemView.setOnClickListener {
            onCardClick?.invoke(cardItem, position)
        }
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    private fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (this * density).toInt()
    }
}