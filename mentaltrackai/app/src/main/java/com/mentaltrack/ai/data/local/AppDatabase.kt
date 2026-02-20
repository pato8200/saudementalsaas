package com.mentaltrack.ai.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mentaltrack.ai.MentalTrackApplication
import com.mentaltrack.ai.data.model.MoodEntry
import com.mentaltrack.ai.util.Converters

@Database(
    entities = [MoodEntry::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moodDao(): MoodDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(application: MentalTrackApplication): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    application.applicationContext,
                    AppDatabase::class.java,
                    "mental_track_database"
                )
                .fallbackToDestructiveMigration() // Recreate database on migration
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}