package com.upn.catatlari.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.upn.catatlari.data.local.dao.UserDao
import com.upn.catatlari.data.local.entity.RunEntity
import com.upn.catatlari.data.local.entity.UserEntity
import com.upn.catatlari.data.local.dao.RunDao

@Database(
    entities = [UserEntity::class, RunEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun runDao(): RunDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "catatlari_db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}