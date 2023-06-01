package com.example.bggstats.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [EntityDataItem::class], version = 1) //entity
abstract class MainDb : RoomDatabase() {
    abstract fun getDao(): Dao

    companion object{
        //создаем базу данных
        fun getDb(context: Context):MainDb {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "test.db"
            ).build()
        }
    }
}