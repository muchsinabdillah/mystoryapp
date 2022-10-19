package com.example.mystoryapp.data.resource.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapp.data.model.LoginEntity
import com.example.mystoryapp.data.model.RegisterEntity
import com.example.mystoryapp.data.model.StoryEntity
import com.example.mystoryapp.data.resource.remote.response.GetAddStoryResponse
import com.example.mystoryapp.data.resource.remote.response.GetLoginResponse
import com.example.mystoryapp.data.resource.remote.response.GetStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteRepository  {

    private val myApiClient = ApiConfig.getApiConfig()

    private val dataLoginresult = MutableLiveData<GetLoginResponse>()
    var userlogin: LiveData<GetLoginResponse> = dataLoginresult

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _dataStoryResult = MutableLiveData<List<StoryEntity>>()
    var datastory: LiveData<List<StoryEntity>> = _dataStoryResult

    companion object {
        @Volatile
        private var instance: RemoteRepository? = null
        fun getInstance(): RemoteRepository =
            instance ?: synchronized(this) {
                instance ?: RemoteRepository()
            }
    }

    fun register(dataregister: RegisterEntity) {

        myApiClient.register(dataregister).enqueue(
            object : Callback<GetAddStoryResponse> {
                override fun onResponse(
                    call: Call<GetAddStoryResponse>,
                    response: Response<GetAddStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            _message.value = responseBody.message
                        }
                    } else {
                        _message.value  = response.message()
                    }
                }
                override fun onFailure(call: Call<GetAddStoryResponse>, t: Throwable) {
                    Log.e("RemoteDataSource", "Failure : ${t.message}")
                }
            }
        )


    }
    fun login(dataLogin: LoginEntity) {
        myApiClient.login(dataLogin).enqueue(
            object : Callback<GetLoginResponse> {
                override fun onResponse(
                    call: Call<GetLoginResponse>,
                    response: Response<GetLoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            dataLoginresult.value =  response.body()

                            _message.value = "Login as ${dataLoginresult.value!!.loginResult.name}"
                        }
                    } else {
                        _message.value = response.message()
                    }
                }
                override fun onFailure(call: Call<GetLoginResponse>, t: Throwable) {
                    Log.e("RemoteDataSource", "Failure : ${t.message}")
                }

            })
    }

    fun allstories(location: Int)  {
        myApiClient.allstories(location).enqueue(
            object : Callback<GetStoryResponse> {
                override fun onResponse(
                    call: Call<GetStoryResponse>,
                    response: Response<GetStoryResponse>
                ) {
                    _dataStoryResult.value = response.body()?.listStory as List<StoryEntity>
                    _message.value = response.message()
                }
                override fun onFailure(call: Call<GetStoryResponse>, t: Throwable) {
                    Log.e("RemoteDataSource", "Data Failure : ${t.message}")
                }
            }
        )
    }
    fun upload(photo: MultipartBody.Part,
               des: RequestBody,
               lat: Double?,
               lng: Double?) {

        myApiClient.uploadImage(photo,des,lat,lng).enqueue(
            object : Callback<GetAddStoryResponse> {
                override fun onResponse(
                    call: Call<GetAddStoryResponse>,
                    response: Response<GetAddStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            _message.value = responseBody.message
                        }
                    } else {
                        _message.value  = response.message()
                    }
                }
                override fun onFailure(call: Call<GetAddStoryResponse>, t: Throwable) {
                    Log.e("RemoteDataSource", "Failure : ${t.message}")
                }
            }
        )


    }
}