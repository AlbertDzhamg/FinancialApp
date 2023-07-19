package com.raywenderlich.financialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.view.MenuItem
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.raywenderlich.financialapp.databinding.ActivityAddTermBinding
import com.raywenderlich.financialapp.databinding.ActivityAddTransactionBinding
import com.raywenderlich.financialapp.terminologyActivity.Terminology
import com.raywenderlich.financialapp.terminologyActivity.TerminologyAppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTermActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTermBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        super.onCreate(savedInstanceState)
        binding = ActivityAddTermBinding.inflate(layoutInflater)
        setContentView(binding.root)


        with(binding){

            termInput.addTextChangedListener {
                if (it!!.count() > 0)
                    termLabel.error = null
            }

            descriptionInput.addTextChangedListener{
                if(it!!.count() > 0)
                    termDescription.error = null
            }

            addTermButton.setOnClickListener {
                val label: String = termInput.text.toString()
                val description: String = descriptionInput.text.toString()

                if(label.isEmpty())
                    termLabel.error = "Неверный ввод"
                else if (description == null)
                    termDescription.error = "Неверный ввод"
                else {
                    val terminology = Terminology(0, label, description)
                    insert(terminology)
                }
            }

        }
    }

    private fun insert(terminology: Terminology){
        val db = Room.databaseBuilder(this@AddTermActivity, TerminologyAppDatabase::class.java, "terminology").build()

        GlobalScope.launch {
            db.termDao().insert(terminology)
            finish()
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



}