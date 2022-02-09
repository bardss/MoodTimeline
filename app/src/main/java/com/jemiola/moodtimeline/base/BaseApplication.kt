package com.jemiola.moodtimeline.base

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jemiola.moodtimeline.model.localdatabase.DatabaseKeys
import com.jemiola.moodtimeline.model.localdatabase.LocalNoSQLDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        AndroidThreeTen.init(this)
        LocalNoSQLDatabase.initNoSQLDatabase(context)
        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            koin.loadModules(
                listOf(
                    mvpViewsModule,
                    mvvmViewsModule
                )
            )
        }
        applyTheme()
    }

    private fun applyTheme() {
        val database = LocalNoSQLDatabase.getInstance()
        val errorValue = 500
        val theme = database?.getInt(DatabaseKeys.APP_THEME, errorValue)
        if (theme != null && theme != errorValue) {
            AppCompatDelegate.setDefaultNightMode(theme)
        }
    }

    companion object {
        lateinit var context: Context
    }

}