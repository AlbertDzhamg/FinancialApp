package com.raywenderlich.financialapp.terminologyActivity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "terminology")
data class Terminology(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val meaning: String
)