package com.raywenderlich.financialapp.terminologyActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.financialapp.R
import com.raywenderlich.financialapp.TransactionActivity.Transaction
import com.raywenderlich.financialapp.databinding.TerminologyItemBinding

class TerminologyAdapter(private var meaningOfWords: List<Terminology>): RecyclerView.Adapter<TerminologyAdapter.terminologyHolder>() {


    class terminologyHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = TerminologyItemBinding.bind(item)
        fun bind(terminology: Terminology) = with(binding){
            terminologyWord.text = terminology.title
            terminologyDescription.text = terminology.meaning
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): terminologyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.terminology_item, parent, false)
        return terminologyHolder(view)
    }

    override fun getItemCount(): Int {
        return meaningOfWords.size
    }

    override fun onBindViewHolder(holder: terminologyHolder, position: Int) {
        val terminology = meaningOfWords[position]
        holder.bind(terminology)
    }

    fun setData(meaningOfWords: List<Terminology>){
        this.meaningOfWords = meaningOfWords
        notifyDataSetChanged()
    }


}