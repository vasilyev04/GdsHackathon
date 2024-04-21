package com.vasilyev.gdshackathon.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vasilyev.gdshackathon.data.local.dao.PlacesDao
import com.vasilyev.gdshackathon.data.local.entity.PlaceEntity

@Database(entities = [PlaceEntity::class], version = 1, exportSchema = false)
abstract class RoomInstance : RoomDatabase() {

    abstract fun placesDao(): PlacesDao

    companion object {
        private var INSTANCE: RoomInstance? = null
        private val LOCK = Any()
        private const val DB_NAME = "places_list.db"

        fun getInstance(application: Application): RoomInstance {
            INSTANCE?.let { return it }

            synchronized(LOCK) {
                INSTANCE?.let { return it }

                val db = Room.databaseBuilder(
                    application,
                    RoomInstance::class.java,
                    DB_NAME)
                    .createFromAsset("database/$DB_NAME")
                    .build()

                INSTANCE = db
                return db
            }
        }
    }
}