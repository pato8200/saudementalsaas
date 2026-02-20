package com.mentaltrack.ai.di

import com.mentaltrack.ai.ai.AiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides
    @Singleton
    fun provideAiService(): AiService {
        return AiService()
    }
}