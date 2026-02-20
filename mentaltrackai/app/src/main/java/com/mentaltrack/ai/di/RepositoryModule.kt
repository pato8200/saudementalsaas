package com.mentaltrack.ai.di

import com.mentaltrack.ai.data.local.MoodDao
import com.mentaltrack.ai.data.repository.MoodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMoodRepository(
        moodDao: MoodDao
    ): MoodRepository {
        return MoodRepository(moodDao)
    }
}