package com.jemiola.moodtimeline.views.splash

import android.util.Log
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.localdatabase.RoomMigrations

class SplashRepository : BaseRepository() {

    private val timelineMoodMigration = RoomMigrations()

    fun executeMigrations(callback: OnRepositoryCallback<Unit>) {
        launchCallbackRequest(
            request = {
                timelineMoodMigration.migrateTimelineMoodFromV1toV2()
            },
            onSuccess = {
                Log.e("Migration: ", "Success")
                callback.onSuccess(Unit)
            },
            onError = {
                Log.e("Migration: ", "Error")
                callback.onError()
            }
        )
    }

}