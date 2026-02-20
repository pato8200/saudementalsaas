package com.mentaltrack.ai

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MentalTrackApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}