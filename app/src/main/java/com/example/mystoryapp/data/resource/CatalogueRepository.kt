package com.example.mystoryapp.data.resource

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.mystoryapp.data.QuoteRemoteMediator
import com.example.mystoryapp.data.model.LoginEntity
import com.example.mystoryapp.data.model.RegisterEntity
import com.example.mystoryapp.data.model.StoryEntity
import com.example.mystoryapp.data.resource.local.room.StoryDatabase
import com.example.mystoryapp.data.resource.remote.ApiService
import com.example.mystoryapp.data.resource.remote.RemoteRepository
import com.example.mystoryapp.data.resource.remote.response.GetLoginResponse
import com.example.mystoryapp.utils.AppExecutors
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CatalogueRepository(
    private val remoteRepository: RemoteRepository,
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
)  {

    companion object {
        fun getInstance(

            remoteRepository: RemoteRepository,
            appExecutors: AppExecutors,
            apiService: ApiService,
            storyDatabase: StoryDatabase
        ): CatalogueRepository {
            return CatalogueRepository(remoteRepository, appExecutors, apiService, storyDatabase)
        }
    }

    fun allstories(location: Int) {
        return remoteRepository.allstories(location)
    }

    fun stories(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = QuoteRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllQuote()
            }
        ).liveData
    }

    fun register(userregister: RegisterEntity) {
        return remoteRepository.register(userregister)
    }
    fun login(userlogin: LoginEntity) {
        return remoteRepository.login(userlogin)
    }

    fun userlogin() : LiveData<GetLoginResponse> {
        return remoteRepository.userlogin
    }
    fun datastory() : LiveData<List<StoryEntity>> {
        return remoteRepository.datastory
    }
    fun message() : LiveData<String> {
        return remoteRepository.message
    }

    fun upload(photo: MultipartBody.Part,
               des: RequestBody,
               lat: Double?,
               lng: Double?) {
        return remoteRepository.upload(photo, des, lat, lng)
    }
}