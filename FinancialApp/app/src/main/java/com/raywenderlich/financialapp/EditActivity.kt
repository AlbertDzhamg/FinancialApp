package com.raywenderlich.financialapp

import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.raywenderlich.financialapp.TransactionActivity.AppDatabase
import com.raywenderlich.financialapp.TransactionActivity.Transaction
import com.raywenderlich.financialapp.TransactionActivity.TransactionAdapter
import com.raywenderlich.financialapp.databinding.ActivityAcountingBinding
import com.raywenderlich.financialapp.databinding.ActivityEditBinding
import com.raywenderlich.financialapp.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.function.DoubleToLongFunction
import kotlin.math.exp

class EditActivity : AppCompatActivity() {
    lateinit private var binding: ActivityEditBinding
    private lateinit var transaction : Transaction


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transaction = intent.getSerializableExtra("transaction") as Transaction

        with(binding) {

            binding.root.setOnClickListener {
                this@EditActivity.window.decorView.clearFocus()

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }

            labelInput.setText(transaction.label)
            amountInput.setText(transaction.amount.toString())
            descriptionInput.setText(transaction.description)


            labelInput.addTextChangedListener {
                updateTransactionBtn.visibility = View.VISIBLE
                if (it!!.count() > 0)
                    labelLayout.error = null
            }

            amountInput.addTextChangedListener {
                updateTransactionBtn.visibility = View.VISIBLE
                if (it!!.count() > 0)
                    amountLayout.error = null
            }

            descriptionInput.addTextChangedListener {
                updateTransactionBtn.visibility = View.VISIBLE
            }

            updateTransactionBtn.setOnClickListener {
                val label: String = labelInput.text.toString()
                val description: String = descriptionInput.text.toString()
                val amount: Double? = amountInput.text.toString().toDoubleOrNull()

                if (label.isEmpty())
                    labelLayout.error = "Неверный тип"
                else if (amount == null)
                    amountLayout.error = "Неверная сумма"
                else {
                    val transaction = Transaction(transaction.id, label, amount, description)
                    update(transaction)
                }
            }
        }
    }


    private fun update(transaction: Transaction) {
        val db = Room.databaseBuilder(
            this@EditActivity,
            AppDatabase::class.java,
            "transactions"
        ).build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
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
}