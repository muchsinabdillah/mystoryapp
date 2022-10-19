package com.example.mystoryapp.data.resource.remote

import com.example.mystoryapp.data.model.LoginEntity
import com.example.mystoryapp.data.model.RegisterEntity
import com.example.mystoryapp.data.resource.remote.response.GetAddStoryResponse
import com.example.mystoryapp.data.resource.remote.response.GetLoginResponse
import com.example.mystoryapp.data.resource.remote.response.GetStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    fun register(@Body payload: RegisterEntity): Call<GetAddStoryResponse>

    @POST("login")
    fun login(@Body payload: LoginEntity): Call<GetLoginResponse>

    @GET("stories")
    suspend fun stories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GetStoryResponse

    @GET("stories")
    fun allstories(
        @Query("location") location: Int
    ): Call<GetStoryResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double?,
        @Part("lon") lon: Double?,
    ): Call<GetAddStoryResponse>
}