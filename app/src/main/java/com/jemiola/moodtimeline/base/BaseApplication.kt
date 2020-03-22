package com.jemiola.moodtimeline.base

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        AndroidThreeTen.init(this)
        startKoin{
            androidLogger()
            androidContext(this@BaseApplication)
            modules(timelineModule, editTimelineItemModule)
        }
    }

    companion object {
        lateinit var context: Context
    }

}