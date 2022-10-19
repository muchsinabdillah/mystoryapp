package com.example.mystoryapp.di

import android.content.Context
import com.example.mystoryapp.data.resource.CatalogueRepository
import com.example.mystoryapp.data.resource.local.room.StoryDatabase
import com.example.mystoryapp.data.resource.remote.ApiConfig
import com.example.mystoryapp.data.resource.remote.RemoteRepository
import com.example.mystoryapp.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): CatalogueRepository {
        val remoteRepository = RemoteRepository.getInstance()
        val appExecutors = AppExecutors()
        val apiService = ApiConfig.getApiConfig()
        val storyDatabase = StoryDatabase.getDatabase(context)
        return CatalogueRepository.getInstance(
            remoteRepository,
            appExecutors,
            apiService,
            storyDatabase
        )
    }
}