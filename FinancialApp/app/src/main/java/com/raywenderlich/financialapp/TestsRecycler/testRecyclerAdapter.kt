package com.raywenderlich.financialapp.TestsRecycler

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.financialapp.QuizActivity
import com.raywenderlich.financialapp.R
import com.raywenderlich.financialapp.SecondaryMainRecycler.phenomenaTypeBooks
import com.raywenderlich.financialapp.databinding.TestRecyclerItemBinding

class testRecyclerAdapter(val testTypeList: ArrayList<testType>): RecyclerView.Adapter<testRecyclerAdapter.testTypeViewHolder>() {

    class testTypeViewHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = TestRecyclerItemBinding.bind(item)
        fun bind(testThemes: testType) = with(binding){
            testItemView.text = testThemes.testName
            rcTestCardView.setCardBackgroundColor(
                when (testThemes.testDifficulty) {
                    3 -> Color.argb(255, 178, 0, 0)
                    2 -> Color.argb(255, 231, 154, 1)
                    else -> Color.argb(255, 76, 175, 80)
                }

            )

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): testTypeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_recycler_item, parent, false)
        return testTypeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return testTypeList.size
    }

    override fun onBindViewHolder(holder: testTypeViewHolder, position: Int) {
        val testThemes = testTypeList[position]
        holder.bind(testThemes)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, QuizActivity::class.java)
            intent.putExtra("quiz_key", testThemes.key)
            val scaleAnimation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_animation)
            holder.itemView.startAnimation(scaleAnimation)
            holder.itemView.context.startActivity(intent)
        }
    }
}