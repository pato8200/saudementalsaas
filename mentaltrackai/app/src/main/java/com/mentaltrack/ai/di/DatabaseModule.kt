package com.mentaltrack.ai.di

import android.content.Context
import com.mentaltrack.ai.data.local.AppDatabase
import com.mentaltrack.ai.data.local.MoodDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context.applicationContext as com.mentaltrack.ai.MentalTrackApplication)
    }

    @Provides
    @Singleton
    fun provideMoodDao(appDatabase: AppDatabase): MoodDao {
        return appDatabase.moodDao()
    }
}