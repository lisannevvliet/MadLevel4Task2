package com.example.madlevel4task2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class History(
    @PrimaryKey
    @ColumnInfo
    val timestamp: Date?,

    @ColumnInfo
    var computerMove: Int,

    @ColumnInfo
    var playerMove: Int,

    @ColumnInfo
    var result: String
)