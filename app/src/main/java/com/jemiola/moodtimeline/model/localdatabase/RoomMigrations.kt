package com.jemiola.moodtimeline.model.localdatabase

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.DatabasesNames
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDO
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDOv2

class RoomMigrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS TimelineMoodDOv2" +
                        "(`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "`note` TEXT NOT NULL," +
                        "`date` INTEGER NOT NULL," +
                        "`mood` TEXT NOT NULL," +
                        "`picturesPaths` TEXT NOT NULL)"
            )
        }
    }

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalDatabase::class.java, DatabasesNames.moodsDatabase
    )
        .addMigrations(MIGRATION_1_2)
        .build()

    fun migrateTimelineMoodFromV1toV2() {
        val moodsV1Count = database.timelineMoodDao().getMoodsCount()
        if (moodsV1Count > 0) {
            val allMoodsV1 = database.timelineMoodDao().getAllMoods()
            val allMoodsV2 = allMoodsV1.toV2()
            allMoodsV2.forEach { v2 ->
                database.timelineMoodDaoV2().insert(v2)
            }
            database.timelineMoodDao().deleteAll()
        }
    }
}

fun List<TimelineMoodDO>.toV2(): List<TimelineMoodDOv2> {
    return map { v1 ->
        TimelineMoodDOv2(
            v1.id,
            v1.note,
            v1.date,
            v1.mood,
            listOf(v1.picturePath)
        )
    }
}