package com.diasandfahri.picbundles.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.diasandfahri.picbundles.data.entity.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 4, exportSchema = false)
abstract class PhotoDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    companion object {
        @Volatile
        private var INSTANCE: PhotoDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): PhotoDatabase {
            if (INSTANCE == null) {
                synchronized(PhotoDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        PhotoDatabase::class.java,
                        "photo_database"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE as PhotoDatabase
        }
    }
}