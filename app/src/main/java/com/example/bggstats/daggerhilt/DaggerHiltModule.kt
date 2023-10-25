package com.example.bggstats.daggerhilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn (SingletonComponent::class)
object DaggerHiltModule {
    @Provides
    @Singleton
    fun provideWiFiManager(settings: WiFiSettings) :WiFiManager {
        return WiFiManager(settings)
    }

    @Provides
    @Singleton
    fun provideWiFiSettings() :WiFiSettings {
        return WiFiSettings()
    }
}