package com.example.bggstats.roomdb

import androidx.room.BuiltInTypeConverters
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Arrays
import java.util.stream.Collectors

//DataBase - Room
@Entity (tableName = "items") //создаем таблицу
data class EntityDataItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "idBGG")
    var idBGG: Int,
    @ColumnInfo(name = "yearPublished")
    var yearPublished: Int,
    @ColumnInfo(name = "nameList")
    var nameList: String//List<String>
)

//?? Проверить - конвертация не работает
class Converters {
    @TypeConverter
    fun listNameToString(nameList: List<String>): String {
        /*var string = ""
        nameList.forEach {
            string = if (string == "") "$it"
            else "$string%%X!!##$it"
        }*/
        return nameList.joinToString("%%X!!##")
    }


    @TypeConverter
    fun stringToListName(data: String): List<String> {
        return listOf(*data.split("%%X!!##".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        //return Arrays.asList(*data.split("%%X!!##".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
    }

    /*val a = listOf("Moscow", "Paris, France", "London")
    val a2 = listOf("London")
    val a3 = emptyList<String>()
    val name2 = a.joinToString("%%X!!##")
    val b = listOf(*name2.split("%%X!!##".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
    val str: List<String> = listOf(*name2.split("%%X!!##").toTypedArray())
    val list: List<String> = name2.split("%%X!!##").toList()*/

    /*@TypeConverter
    fun fromHobbies(hobbies: List<String?>): String {
        return hobbies.stream().collect(Collectors.joining(","))
    }

    @TypeConverter
    fun fromHobbies2(hobbies: List<String?>) = hobbies.stream().collect(Collectors.joining(","))


    @TypeConverter
    fun toHobbies(data: String): List<String> {
        return Arrays.asList(*data.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
    }

    @TypeConverter
    fun teacherToString(teacher: Teacher) = "$teacher" //Other options are json string, serialized blob

    @TypeConverter
    fun stringToTeacher(value: String): Teacher {
        val name = value.substringBefore(':')
        val age = value.substringAfter(':').toInt()

        return Teacher(name, age)
    }*/
}


