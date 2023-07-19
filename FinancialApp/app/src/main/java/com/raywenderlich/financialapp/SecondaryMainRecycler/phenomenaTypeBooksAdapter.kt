package com.raywenderlich.financialapp.SecondaryMainRecycler

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.financialapp.MainRecycler.phenomenaTypes
import com.raywenderlich.financialapp.QuizActivity
import com.raywenderlich.financialapp.R
import com.raywenderlich.financialapp.SecondaryActivity
import com.raywenderlich.financialapp.databinding.SecondaryRcPhenomenaBookTypeBinding
import com.raywenderlich.financialapp.databinding.SecondaryRcActivityBinding
import com.squareup.picasso.Picasso

class phenomenaTypeBooksAdapter(val phenomenaTypeBooksList: ArrayList<phenomenaTypeBooks>): RecyclerView.Adapter<phenomenaTypeBooksAdapter.PhenomenaTypeBooksHolder>() {

    fun filterList(filteredList: List<phenomenaTypeBooks>) {
        phenomenaTypeBooksList.clear()
        phenomenaTypeBooksList.addAll(filteredList)
        notifyDataSetChanged()
    }

    class PhenomenaTypeBooksHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = SecondaryRcPhenomenaBookTypeBinding.bind(item)
        fun bind(phenomenaTypeBook: phenomenaTypeBooks) = with(binding){
            phenomenaItemBookView.text = phenomenaTypeBook.title
            phenomenaItemAuthorName.text = phenomenaTypeBook.author
            phenomenaItemPagesNumber.text = phenomenaTypeBook.pages.toString()
            phenomenaItemDateNumber.text = phenomenaTypeBook.date.toString()
            if (!phenomenaTypeBook.imageUrl.isNullOrEmpty()) { // Проверяем, есть ли изображение
                Picasso.get().load(phenomenaTypeBook.imageUrl).into(rcImageView)
            } else {
                rcImageView.setImageResource(R.drawable.no_image) // устанавливаем стандартное изображение
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhenomenaTypeBooksHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.secondary_rc_phenomena_book_type, parent, false)
        return PhenomenaTypeBooksHolder(view)
    }

    override fun getItemCount(): Int {
        return phenomenaTypeBooksList.size
    }

    override fun onBindViewHolder(holder: PhenomenaTypeBooksHolder, position: Int) {
        holder.bind(phenomenaTypeBooksList[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, QuizActivity::class.java)
            val scaleAnimation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_animation)
            holder.itemView.startAnimation(scaleAnimation)
        }
    }

}