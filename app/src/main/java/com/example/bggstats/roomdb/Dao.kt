package com.example.bggstats.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    //помесить
    //@Insert //по умолчанию
    @Insert(onConflict = OnConflictStrategy.REPLACE) //Temp - проверить
    fun insertItem(item: EntityDataItem) //(vararg classrooms: Classroom)
    /*//прочитать (без отслеживания изменений)
    @Query("SELECT * FROM items") //чтоСделать-ВЫБРАТЬ(SELECT) что-ВСЁ(*) откуда-ИЗ(FROM) таблицы"items"
    fun getAllItems(): List<DataItem>*/
    //прочитать (c отслеживанием изменений)
    @Query("SELECT * FROM items") //чтоСделать-ВЫБРАТЬ(SELECT) что-ВСЁ(*) откуда-ИЗ(FROM) таблицы"items"
    fun getAllItems(): Flow<List<EntityDataItem>>

    //очищаем таблицу
    @Query("DELETE FROM items")
    fun deleteAll()
}