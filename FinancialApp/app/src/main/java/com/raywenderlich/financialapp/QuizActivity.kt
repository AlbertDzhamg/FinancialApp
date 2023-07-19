package com.raywenderlich.financialapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.raywenderlich.financialapp.SecondaryMainRecycler.phenomenaTypeBooks
import com.raywenderlich.financialapp.SecondaryMainRecycler.phenomenaTypeBooksAdapter
import com.raywenderlich.financialapp.databinding.ActivityQuizBinding
import org.w3c.dom.Text

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    lateinit var mQuestionList: ArrayList<Question?>
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0

    lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val quizKey = intent.getStringExtra("quiz_key")
        val database = Firebase.database
        val ref = database.getReference("Tests").child("Variations").child("Test1").child(quizKey.toString()).child("Questions")

        mQuestionList = arrayListOf()

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mQuestionList.clear()

                for (bookSnapshot in snapshot.children){
                    val id = bookSnapshot.child("id").getValue(Int::class.java)
                    val question = bookSnapshot.child("question").getValue(String::class.java)
                    val option1 = bookSnapshot.child("optionOne").getValue(String::class.java)
                    val option2 = bookSnapshot.child("optionTwo").getValue(String::class.java)
                    val option3 = bookSnapshot.child("optionThree").getValue(String::class.java)
                    val option4 = bookSnapshot.child("optionFour").getValue(String::class.java)
                    val correctAns = bookSnapshot.child("correctAnswer").getValue(Int::class.java)
                    var questionEx = Question(id, question, option1, option2, option3, option4, correctAns)
                    mQuestionList.add(questionEx)
                    Log.i("firebase", "Got value ${mQuestionList}")
                }

                setQuestion()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error getting books data: ${error.message}")
            }
        })

        with(binding){
            tvOptionOne.setOnClickListener(this@QuizActivity)
            tvOptionTwo.setOnClickListener(this@QuizActivity)
            tvOptionThree.setOnClickListener(this@QuizActivity)
            tvOptionFour.setOnClickListener(this@QuizActivity)
            btnSubmit.setOnClickListener(this@QuizActivity)
        }




    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // обработка нажатия кнопки навигации
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setQuestion() = with(binding){

        val question = mQuestionList!![mCurrentPosition-1]

        defaultOptionsView()

        if (mCurrentPosition == mQuestionList!!.size){
            btnSubmit.text = "Закончить"
        } else {
            btnSubmit.text = "Выбрать"
        }

        progressBar.max = mQuestionList.size
        progressBar.progress = mCurrentPosition
        tvProgress.text = "$mCurrentPosition" + "/" + mQuestionList.size
        tvQuestion.text = question!!.question
        tvOptionOne.text= question!!.optionOne
        tvOptionTwo.text= question!!.optionTwo
        tvOptionThree.text= question!!.optionThree
        tvOptionFour.text= question!!.optionFour
    }
    private fun defaultOptionsView() = with(binding){
        val options = ArrayList<TextView>()
        options.add(0, tvOptionOne)
        options.add(1, tvOptionTwo)
        options.add(2, tvOptionThree)
        options.add(3, tvOptionFour)

        for (option in options){
            option.setTextColor(Color.parseColor("#000000"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this@QuizActivity, R.drawable.default_option_border_bg)
        }

    }

    override fun onClick(v: View?) = with(binding) {
        when(v?.id){
            R.id.tv_option_one -> { selectedOptionView(tvOptionOne, 1)}
            R.id.tv_option_two -> { selectedOptionView(tvOptionTwo, 2)}
            R.id.tv_option_three -> { selectedOptionView(tvOptionThree, 3)}
            R.id.tv_option_four -> { selectedOptionView(tvOptionFour, 4)}
            R.id.btn_submit -> {
                if(mSelectedOptionPosition == 0){
                    mCurrentPosition++

                    when{
                        mCurrentPosition <= mQuestionList!!.size -> {
                            setQuestion()
                        }else->{
                            val intent = Intent(this@QuizActivity, ResultActivity::class.java)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionList!!.size)
                            startActivity(intent)
                        }
                    }
                } else{
                    val question = mQuestionList?.get(mCurrentPosition - 1)
                    if(question!!.correctAnswer != mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    } else{
                        mCorrectAnswers++
                    }
                    question.correctAnswer?.let { answerView(it, R.drawable.correct_option_border_bg) }

                    if(mCurrentPosition == mQuestionList!!.size){
                        btnSubmit.text = "Закончить"
                    }
                    else {
                        btnSubmit.text = "Следующий"
                    }
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int) = with(binding){
        when(answer) {
            1 -> { tvOptionOne.background = ContextCompat.getDrawable(this@QuizActivity, drawableView) }
            2 -> { tvOptionTwo.background = ContextCompat.getDrawable(this@QuizActivity, drawableView) }
            3 -> { tvOptionThree.background = ContextCompat.getDrawable(this@QuizActivity, drawableView) }
            4 -> { tvOptionFour.background = ContextCompat.getDrawable(this@QuizActivity, drawableView) }
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int){
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this@QuizActivity, R.drawable.selected_option_border_bg)
    }

}