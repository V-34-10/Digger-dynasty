package com.diggingdy.nastyslo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.diggingdy.nastyslo.R
import com.diggingdy.nastyslo.model.Card

class CardAdapter(private val cardList: List<Card>) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    private var onCardClick: ((Card, Int) -> Unit)? = null

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardImage: ImageView = itemView.findViewById(R.id.cardImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_image, parent, false)
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
}