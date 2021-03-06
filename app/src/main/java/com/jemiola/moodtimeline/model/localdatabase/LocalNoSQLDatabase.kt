package com.jemiola.moodtimeline.model.localdatabase

import android.content.Context
import android.content.SharedPreferences


private const val SHARED_PREFERENCES_KEY = "mood_timeline_shared_preferences"

object LocalNoSQLDatabase {

    private var database: SharedPreferences? = null

    fun initNoSQLDatabase(context: Context) {
        database = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

    fun getInstance(): SharedPreferences? = database
}