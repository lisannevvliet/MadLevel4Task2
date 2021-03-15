package com.example.madlevel4task2

import android.content.Context

class HistoryRepository(context: Context) {
    private val historyDao: HistoryDao

    init {
        val database = HistoryRoomDatabase.getDatabase(context)
        historyDao = database!!.historyDao()
    }

    suspend fun getAll(): List<History> = historyDao.getAll()

    suspend fun getWin(): Int = historyDao.getWin()

    suspend fun getDraw(): Int = historyDao.getDraw()

    suspend fun getLose(): Int = historyDao.getLose()

    suspend fun insert(history: History) { historyDao.insert(history) }

    suspend fun deleteAll() { historyDao.deleteAll() }
}