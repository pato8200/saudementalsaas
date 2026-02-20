package com.mentaltrack.ai.data.repository

import com.mentaltrack.ai.data.local.MoodDao
import com.mentaltrack.ai.data.model.MoodEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoodRepository @Inject constructor(
    private val moodDao: MoodDao
) {
    fun getAllMoodEntries(): Flow<List<MoodEntry>> = moodDao.getAllMoodEntries()

    fun getMoodEntriesByDateRange(startDate: Long, endDate: Long): Flow<List<MoodEntry>> = 
        moodDao.getMoodEntriesByDateRange(startDate, endDate)

    suspend fun insertMoodEntry(moodEntry: MoodEntry) = moodDao.insertMoodEntry(moodEntry)

    suspend fun updateMoodEntry(moodEntry: MoodEntry) = moodDao.updateMoodEntry(moodEntry)

    suspend fun deleteMoodEntry(moodEntry: MoodEntry) = moodDao.deleteMoodEntry(moodEntry)

    suspend fun getMoodEntryById(id: String): MoodEntry? = moodDao.getMoodEntryById(id)
}