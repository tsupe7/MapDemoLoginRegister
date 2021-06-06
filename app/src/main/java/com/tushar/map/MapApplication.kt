package com.tushar.map

import android.app.Application

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MapApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}