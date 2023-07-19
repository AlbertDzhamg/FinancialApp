package com.raywenderlich.financialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.raywenderlich.financialapp.SecondaryMainRecycler.phenomenaTypeBooks
import com.raywenderlich.financialapp.SecondaryMainRecycler.phenomenaTypeBooksAdapter
import com.raywenderlich.financialapp.TestsRecycler.testRecyclerAdapter
import com.raywenderlich.financialapp.TestsRecycler.testType
import com.raywenderlich.financialapp.databinding.ActivityTestRecyclerBinding

class TestRecycler : AppCompatActivity() {
    lateinit var adapter: testRecyclerAdapter
    lateinit var binding: ActivityTestRecyclerBinding
    lateinit var testNameArrayList: ArrayList<testType>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val database = Firebase.database
        val ref = database.getReference("Tests").child("Variations").child("Test1")
        testNameArrayList = arrayListOf()

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                testNameArrayList.clear()
                for (bookSnapshot in snapshot.children){
                    val testName = bookSnapshot.child("name").getValue(String::class.java) // Получаем значение поля "title"
                    val testId = bookSnapshot.child("testId").getValue(Int::class.java)
                    val testId1 = bookSnapshot.key
                    val testDifficulty = bookSnapshot.child("testDifficulty").getValue(Int::class.java)
                    var testType = testType(testId1, testId, testName, testDifficulty)
                    testNameArrayList.add(testType)
                    Log.i("firebase", "Got value $testNameArrayList")
               }
                adapter = testRecyclerAdapter(testNameArrayList)
                init()
            }
           override fun onCancelled(error: DatabaseError) {
               Log.e("Firebase", "Error getting books data: ${error.message}")
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
        binding.rcTestView.layoutManager = LinearLayoutManager(this)
        binding.rcTestView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}