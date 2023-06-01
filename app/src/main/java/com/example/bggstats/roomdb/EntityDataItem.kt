package com.example.bggstats.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bggstats.items.Name

@Entity (tableName = "items") //создаем таблицу
data class EntityDataItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "idBGG")
    var idBGG: Int,
    @ColumnInfo(name = "yearPablished")
    var yearPablished: Int,
    //@ColumnInfo(name = "nameList")
    //var nameList: List<Name>
)
