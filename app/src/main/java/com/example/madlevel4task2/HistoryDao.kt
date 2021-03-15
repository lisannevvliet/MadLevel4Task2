package com.example.madlevel4task2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM History")
    suspend fun getAll(): List<History>

    @Query("SELECT COUNT(*) FROM History WHERE result = 'You win!'")
    suspend fun getWin(): Int

    @Query("SELECT COUNT(*) FROM History WHERE result = 'Draw'")
    suspend fun getDraw(): Int

    @Query("SELECT COUNT(*) FROM History WHERE result = 'Computer wins!'")
    suspend fun getLose(): Int

    @Insert
    suspend fun insert(history: History)

    @Query("DELETE FROM History")
    suspend fun deleteAll()
}