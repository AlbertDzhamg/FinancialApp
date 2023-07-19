package com.raywenderlich.financialapp.TransactionActivity

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.financialapp.EditActivity
import com.raywenderlich.financialapp.R
import com.raywenderlich.financialapp.databinding.TransactionItemLayoutBinding

class TransactionAdapter(private var transactions: List<Transaction>): RecyclerView.Adapter<TransactionAdapter.transactionHolder>() {

    class transactionHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = TransactionItemLayoutBinding.bind(item)
        fun bind(transactions: Transaction) = with(binding){
            label.text = transactions.label
            amount.text = transactions.amount.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): transactionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item_layout, parent, false)
        return transactionHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: transactionHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)

        val formattedAmount = "%.2f".format(Math.abs(transaction.amount))
        if (transaction.amount >= 0) {
            holder.binding.amount.text = "+ $formattedAmount"
            holder.binding.amount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
        } else {
            holder.binding.amount.text = "- $formattedAmount"
            holder.binding.amount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        }

        holder.binding.label.text = transaction.label
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditActivity::class.java)
            intent.putExtra("transaction", transaction)
            holder.itemView.context.startActivity(intent)
        }


    }

    fun setData(transactions: List<Transaction>){
        this.transactions = transactions
        notifyDataSetChanged()
    }

}