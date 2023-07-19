package com.raywenderlich.financialapp.terminologyActivity

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Terminology::class), version = 1)
abstract class TerminologyAppDatabase : RoomDatabase() {
    abstract fun termDao(): TerminologyDao
}