package com.example.bloggers

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BloggersApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
