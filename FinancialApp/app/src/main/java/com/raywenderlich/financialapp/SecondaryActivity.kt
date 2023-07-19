package com.raywenderlich.financialapp

import android.os.Bundle
import android.renderscript.Sampler.Value
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.raywenderlich.financialapp.SecondaryMainRecycler.phenomenaTypeBooks
import com.raywenderlich.financialapp.SecondaryMainRecycler.phenomenaTypeBooksAdapter
import com.raywenderlich.financialapp.databinding.ActivityMainBinding
import com.raywenderlich.financialapp.databinding.SecondaryRcActivityBinding
import kotlin.math.log

class SecondaryActivity: AppCompatActivity() {

    private lateinit var binding: SecondaryRcActivityBinding
    private lateinit var adapter: phenomenaTypeBooksAdapter
    private var booksArrayList: ArrayList<phenomenaTypeBooks> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        super.onCreate(savedInstanceState)
        binding = SecondaryRcActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bookType = intent.getStringExtra("bookType")
        val database = Firebase.database
        val ref = database.getReference("Books").child("Variations").child(bookType.toString())

        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                booksArrayList.clear()
                for (bookSnapshot in snapshot.children){
                    val bookTitle = bookSnapshot.child("title").getValue(String::class.java) // Получаем значение поля "title"
                    val bookImageUrl = bookSnapshot.child("imageUrl").getValue(String::class.java) // Получаем значение поля "imageUrl"
                    val bookPages = bookSnapshot.child("pages").getValue(Int::class.java) // Получаем значение поля "title"
                    val bookauthor = bookSnapshot.child("author").getValue(String::class.java)
                    val date = bookSnapshot.child("date").getValue(Int::class.java)
                    var phenomenaTypeBooks = phenomenaTypeBooks(bookImageUrl, bookTitle, bookauthor, bookPages, date)
                    booksArrayList.add(phenomenaTypeBooks)
                    Log.i("firebase", "Got value ${booksArrayList}")
                }
                adapter = phenomenaTypeBooksAdapter(ArrayList(booksArrayList))
                init()

                // Сортировка списка по title
                binding.searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val filterList = booksArrayList.filter { it.title!!.contains(s.toString(), ignoreCase = true) }
                        adapter.filterList(filterList)
                    }
                })
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

    private fun init() {
        binding.rcSecondaryView.layoutManager = LinearLayoutManager(this)
        binding.rcSecondaryView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}