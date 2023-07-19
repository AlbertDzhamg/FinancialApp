package com.raywenderlich.financialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.raywenderlich.financialapp.TransactionActivity.Transaction
import com.raywenderlich.financialapp.databinding.ActivityTerminologyBinding
import com.raywenderlich.financialapp.terminologyActivity.Terminology
import com.raywenderlich.financialapp.terminologyActivity.TerminologyAdapter
import com.raywenderlich.financialapp.terminologyActivity.TerminologyAppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.nio.file.Files.delete

class TerminologyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTerminologyBinding
    private lateinit var meaningOfWords: List<Terminology>
    private lateinit var adapter: TerminologyAdapter
    private lateinit var db: TerminologyAppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        super.onCreate(savedInstanceState)
        binding = ActivityTerminologyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        meaningOfWords = arrayListOf()

        adapter = TerminologyAdapter(meaningOfWords)

        db = Room.databaseBuilder(this@TerminologyActivity, TerminologyAppDatabase::class.java, "terminology").build()

        init()

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTermin(meaningOfWords[viewHolder.adapterPosition])
            }

        })


        itemTouchHelper.attachToRecyclerView(binding.rcTerminologyView)

        with(binding) {
            addBtn.setOnClickListener {
                val intent = Intent(this@TerminologyActivity, AddTermActivity::class.java)
                startActivity(intent)
            }
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchTerm = s.toString().trim()
                search(searchTerm)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun init() {
        binding.rcTerminologyView.layoutManager = LinearLayoutManager(this)
        binding.rcTerminologyView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun fetchAll() {
        GlobalScope.launch {
            meaningOfWords = db.termDao().getAll()

            runOnUiThread {
                adapter.setData(meaningOfWords)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchAll()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteTermin(termin: Terminology) {
        GlobalScope.launch {
            db.termDao().delete(termin)
            fetchAll()
        }
    }

    private fun search(searchTerm: String) {
        GlobalScope.launch {
            val searchResults = db.termDao().searchByTitle("%$searchTerm%")
            runOnUiThread {
                adapter.setData(searchResults)
                adapter.notifyDataSetChanged()
            }
        }
    }
}

