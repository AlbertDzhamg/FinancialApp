package com.raywenderlich.financialapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.raywenderlich.financialapp.MainRecycler.phenomenaTypes
import com.raywenderlich.financialapp.MainRecycler.phenomenaTypesAdapter
import com.raywenderlich.financialapp.SecondaryMainRecycler.phenomenaTypeBooks
import com.raywenderlich.financialapp.databinding.ActivityMainBinding
import com.raywenderlich.financialapp.databinding.PhenomenaItemBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val database = Firebase.database
        val ref = database.getReference("Tests").child("Variations").child("Test1")
        init()


    }

    private fun init(){
        val buttonClick = binding.studyMaterials
        buttonClick.setOnClickListener{
            val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
            buttonClick.startAnimation(scaleAnimation)
            val intent = Intent(this, LearningTopics::class.java)
            startActivity(intent)
        }

        val buttonClick2 = binding.testMaterials
        buttonClick2.setOnClickListener {
            val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
            buttonClick.startAnimation(scaleAnimation)
            val intent = Intent(this, TestRecycler::class.java)
            startActivity(intent)
        }

        val buttonClick3 = binding.accountingMaterials
        buttonClick3.setOnClickListener {
            val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
            buttonClick.startAnimation(scaleAnimation)
            val intent = Intent(this, AcountingActivity::class.java)
            startActivity(intent)
        }

        val buttonClick4 = binding.addTest
        buttonClick4.setOnClickListener {

            val intent = Intent(this, webviewpdfreaderActivity::class.java)
            startActivity(intent)
        }

        val buttonClick5 = binding.terminologyBtn
        buttonClick5.setOnClickListener {
            val intent = Intent(this, TerminologyActivity::class.java)
            startActivity(intent)
        }

    }
}