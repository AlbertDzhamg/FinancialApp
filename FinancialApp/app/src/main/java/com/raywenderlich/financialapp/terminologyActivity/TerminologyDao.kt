package com.raywenderlich.financialapp.terminologyActivity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.raywenderlich.financialapp.TransactionActivity.Transaction

@Dao
interface TerminologyDao {
    @Query("SELECT * FROM terminology")
    fun getAll(): List<Terminology>

    @Insert
    fun insert(term: Terminology)

    @Query("SELECT * FROM terminology WHERE title LIKE :searchTerm OR meaning LIKE :searchTerm")
    fun searchByTitle(searchTerm: String): List<Terminology>

    @Delete
    fun delete(term: Terminology)
}