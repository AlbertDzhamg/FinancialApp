package com.raywenderlich.financialapp.MainRecycler

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.financialapp.R
import com.raywenderlich.financialapp.SecondaryActivity
import com.raywenderlich.financialapp.databinding.PhenomenaItemBinding

class phenomenaTypesAdapter: RecyclerView.Adapter<phenomenaTypesAdapter.PhenomenaTypesHolder>() {

    val phenomenaTypesList = ArrayList<phenomenaTypes>()

    class PhenomenaTypesHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = PhenomenaItemBinding.bind(item)
        fun bind(phenomenaType: phenomenaTypes) = with(binding){
            phenomenaItemView.text = phenomenaType.title
            rcMainCardView.setCardBackgroundColor(Color.parseColor(phenomenaType.backgroundCode))
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhenomenaTypesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.phenomena_item, parent, false)
        return PhenomenaTypesHolder(view)
    }


    override fun getItemCount(): Int {
        return phenomenaTypesList.size
    }

    override fun onBindViewHolder(holder: PhenomenaTypesHolder, position: Int) {
        val phenomenaType = phenomenaTypesList[position]
        holder.bind(phenomenaType)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, SecondaryActivity::class.java)
            val scaleAnimation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_animation)
            holder.itemView.startAnimation(scaleAnimation)
            intent.putExtra("bookType", phenomenaType.bookType)
            holder.itemView.context.startActivity(intent)
        }
    }

    fun addPhenomen(phenomen: phenomenaTypes){
        phenomenaTypesList.add(phenomen)
        notifyDataSetChanged()
    }
}
