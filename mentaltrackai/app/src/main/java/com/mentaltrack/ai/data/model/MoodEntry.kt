package com.mentaltrack.ai.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val moodLevel: Int, // 1-5 scale (1 = very unhappy, 5 = very happy)
    val activities: List<String>, // List of activity IDs/tags
    val note: String = "",
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val createdAt: Long = System.currentTimeMillis()
)