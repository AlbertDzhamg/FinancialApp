package com.raywenderlich.financialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.raywenderlich.financialapp.TransactionActivity.AppDatabase
import com.raywenderlich.financialapp.TransactionActivity.Transaction
import com.raywenderlich.financialapp.TransactionActivity.TransactionAdapter
import com.raywenderlich.financialapp.databinding.ActivityAcountingBinding
import com.raywenderlich.financialapp.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.function.DoubleToLongFunction
import kotlin.math.exp

class AcountingActivity : AppCompatActivity() {

    private lateinit var deletedTransaction: Transaction
    private lateinit var oldTransactions: List<Transaction>
    lateinit var binding: ActivityAcountingBinding
    private lateinit var transactions: List<Transaction>
    private lateinit var adapter: TransactionAdapter
    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        super.onCreate(savedInstanceState)
        binding = ActivityAcountingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactions = arrayListOf()

        adapter = TransactionAdapter(transactions)

        db = Room.databaseBuilder(this@AcountingActivity, AppDatabase::class.java, "transactions").build()

        fetchAll()

        init()


        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactions[viewHolder.adapterPosition])
            }

        }


        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(binding.rcLabels)

        with(binding){
            addBtn.setOnClickListener{
                val intent = Intent(this@AcountingActivity, AddTransactionActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun init(){
        binding.rcLabels.layoutManager = LinearLayoutManager(this)
        binding.rcLabels.adapter = adapter
        adapter.notifyDataSetChanged()
        updateDashboard()
    }

    private fun fetchAll() = with(binding){
        GlobalScope.launch  {
            transactions = db.transactionDao().getAll()

            runOnUiThread {
                adapter.setData(transactions)
                updateDashboard()
            }
        }
    }

    private fun updateDashboard(){
        val totalAmount: Double = transactions.map { it.amount }.sum()
        val budgetAmount: Double = transactions.filter{ it.amount > 0 }.map{ it.amount }.sum()
        val expenseAmount: Double = totalAmount - budgetAmount

        with(binding){
            balance.text = "%.0f".format(totalAmount)
            budget.text = "%.0f RUB".format(budgetAmount)
            expense.text = "%.0f RUB".format(expenseAmount)
        }


    }

    private fun deleteTransaction(transaction: Transaction){
        deletedTransaction = transaction
        oldTransactions = transactions

        GlobalScope.launch {
            db.transactionDao().delete(transaction)

            transactions = transactions.filter { it.id  != transaction.id }
            runOnUiThread {
                updateDashboard()
                adapter.setData(transactions)
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
}