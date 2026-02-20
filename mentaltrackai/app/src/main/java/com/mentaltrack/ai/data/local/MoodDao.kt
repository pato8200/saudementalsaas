package com.mentaltrack.ai.data.local

import androidx.room.*
import com.mentaltrack.ai.data.model.MoodEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_entries ORDER BY createdAt DESC")
    fun getAllMoodEntries(): Flow<List<MoodEntry>>

    @Query("SELECT * FROM mood_entries WHERE id = :id")
    suspend fun getMoodEntryById(id: String): MoodEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoodEntry(moodEntry: MoodEntry)

    @Update
    suspend fun updateMoodEntry(moodEntry: MoodEntry)

    @Delete
    suspend fun deleteMoodEntry(moodEntry: MoodEntry)

    @Query("SELECT * FROM mood_entries WHERE createdAt >= :startDate AND createdAt <= :endDate ORDER BY createdAt DESC")
    fun getMoodEntriesByDateRange(startDate: Long, endDate: Long): Flow<List<MoodEntry>>

    @Query("SELECT * FROM mood_entries WHERE moodLevel = :moodLevel ORDER BY createdAt DESC")
    fun getMoodEntriesByMoodLevel(moodLevel: Int): Flow<List<MoodEntry>>

    @Query("DELETE FROM mood_entries WHERE createdAt < :timestamp")
    suspend fun deleteMoodEntriesOlderThan(timestamp: Long)
}