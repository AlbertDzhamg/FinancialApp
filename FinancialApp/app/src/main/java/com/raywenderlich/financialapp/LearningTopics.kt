package com.raywenderlich.financialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.raywenderlich.financialapp.MainRecycler.phenomenaTypes
import com.raywenderlich.financialapp.MainRecycler.phenomenaTypesAdapter
import com.raywenderlich.financialapp.databinding.ActivityLearningTopicsBinding
import com.raywenderlich.financialapp.databinding.ActivityMainBinding

class LearningTopics : AppCompatActivity() {

    lateinit var binding: ActivityLearningTopicsBinding
    private val adapter = phenomenaTypesAdapter()
    private var index = 0
    val phenomenaTypesList = listOf(
        "#183D29",
        "#1B4A34", // розовый цвет
        "#235C3F",  // желтый цвет
        "#2F7151",
        "#3E8664", // розовый цвет
        "#2F7151",
        "#235C3F",
        "#1B4A34", // розовый цвет
        "#183D29",
    )

    val phenomenaTypesListNames = listOf(
        "Основы финансовых знаний",
        "Доходы",
        "Управление расходами",
        "Личные сбережения",
        "Займы и кредиты",
        "Инвестирование",
        "Страхование",
        "Финансовые риски и безопасность",
        "Защита прав потребителей"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearningTopicsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        onBackPressedDispatcher.addCallback(this /* lifecycle owner */, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Back is pressed... Finishing the activity
                finish()
            }
        })

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

    private fun init(){
        binding.apply {
            rcTopicView.layoutManager = LinearLayoutManager(this@LearningTopics)
            rcTopicView.adapter = adapter
            button2.setOnClickListener{
                onBackPressedDispatcher.onBackPressed()
            }

            for (i in 0 until phenomenaTypesList.count()) {
                val phenomen = phenomenaTypes("type${i}",phenomenaTypesListNames[i], phenomenaTypesList[i])
                adapter.addPhenomen(phenomen)
            }
        }
    }
}