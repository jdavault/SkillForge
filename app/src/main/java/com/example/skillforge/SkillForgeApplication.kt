package com.example.skillforge

import android.app.Application
import com.example.skillforge.data.starter.ContentSeeder
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class SkillForgeApplication : Application() {

    @Inject
    lateinit var contentSeeder: ContentSeeder

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            contentSeeder.seedIfEmpty()
        }
    }
}