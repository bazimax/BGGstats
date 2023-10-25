package com.example.bggstats.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//База данных
//DataBase - Room
@Database (entities = [EntityDataItem::class], version = 2) //entity
abstract class MainDb : RoomDatabase() {

    /*
    //?? Проверить - конвертация не работает
    @TypeConverters(
        Converters::class
    )*/
    abstract fun getDao(): Dao

    companion object{
        //создаем базу данных
        //!! Важно - деструктивное обновление БД (через удаление предыдущей)
        fun getDb(context: Context):MainDb {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "test.db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}