package com.example.bggstats.daggerhilt

import com.example.bggstats.atest.logD
import javax.inject.Inject
import javax.inject.Singleton

//private const val lnc = "WiFiManager" //logNameClass - для логов


class WiFiManager(private val settings: WiFiSettings) {
    fun connect(){
        settings.openConnection()
    }

    fun sendMessage(){
        settings.writeBytes()
    }
}


class WiFiSettings {
    fun openConnection(){
        logD("Connected")
    }

    fun writeBytes(){
        logD("Hello!")
    }
}

//Backup
/*
//Variant 1 - Variant 2 in DaggerHiltModule.kt
@Singleton
class WiFiManager @Inject constructor(private val settings: WiFiSettings) {
    fun connect(){
        settings.openConnection()
    }

    fun sendMessage(){
        settings.writeBytes()
    }
}


class WiFiSettings @Inject constructor() {
    fun openConnection(){
        logD("Connected")
    }

    fun writeBytes(){
        logD("Hello!")
    }
}*/
