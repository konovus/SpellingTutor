package com.example.spellingnotify.data.di

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.spellingnotify.data.api.DictionaryApi
import com.example.spellingnotify.data.notification.NotificationHelper
import com.example.spellingnotify.data.notification.NotificationManagerImpl
import com.example.spellingnotify.data.preferences.SettingsManagerImpl
import com.example.spellingnotify.data.repository.MainRepositoryImpl
import com.example.spellingnotify.domain.notification.NotificationManager
import com.example.spellingnotify.domain.repository.MainRepository
import com.example.spellingnotify.domain.usecases.MainFilterUseCase
import com.example.spellingnotify.domain.utils.SettingsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TestAppModule {

    @Singleton
    @Provides
    fun provideSettingsManager(app: Application): SettingsManager {
        return SettingsManagerImpl(app)
    }

    @Singleton
    @Provides
    fun provideNotificationWorkManager(app: Application): NotificationManager {
        return NotificationManagerImpl(app)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Singleton
    @Provides
    fun provideNotificationHelper(
        app: Application,
        settingsManager: SettingsManager,
        repository: MainRepository,
        mainFilterUseCase: MainFilterUseCase
    ): NotificationHelper {
        return NotificationHelper(app, settingsManager, repository, mainFilterUseCase)
    }

    @Provides
    @Singleton
    fun provideDictionaryApi(): DictionaryApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.dictionaryapi.dev/api/v2/entries/en/")
            .build()
            .create(DictionaryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMainRepository(dictionaryApi: DictionaryApi): MainRepository {
        return MainRepositoryImpl(dictionaryApi)
    }

    @Singleton
    @Provides
    fun provideMainFilterUseCase(settingsManager: SettingsManager): MainFilterUseCase{
        return MainFilterUseCase(settingsManager)
    }

}