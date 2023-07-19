package com.raywenderlich.financialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.raywenderlich.financialapp.TransactionActivity.AppDatabase
import com.raywenderlich.financialapp.TransactionActivity.Transaction
import com.raywenderlich.financialapp.databinding.ActivityAddTransactionBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {


    lateinit private var binding: ActivityAddTransactionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){


            labelInput.addTextChangedListener {
                if (it!!.count() > 0)
                    labelLayout.error = null
            }

            amountInput.addTextChangedListener {
                if(it!!.count() > 0)
                    amountLayout.error = null
            }

            addTransactionButton.setOnClickListener{
                val label: String = labelInput.text.toString()
                val description: String = descriptionInput.text.toString()
                val amount: Double? = amountInput.text.toString().toDoubleOrNull()

                if (label.isEmpty())
                    labelLayout.error = "Неверный тип"
                else if (amount == null)
                    amountLayout.error = "Неверная сумма"
                else {
                    val transaction = Transaction(0, label, amount, description)
                    insert(transaction)
                }
            }
        }
    }


    private fun insert(transaction: Transaction){
        val db = Room.databaseBuilder(this@AddTransactionActivity, AppDatabase::class.java, "transactions").build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
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