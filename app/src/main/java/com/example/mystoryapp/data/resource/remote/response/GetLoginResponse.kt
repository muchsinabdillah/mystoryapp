package com.example.mystoryapp.data.resource.remote.response

import com.example.mystoryapp.data.model.Login
import com.google.gson.annotations.SerializedName

class GetLoginResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("loginResult")
    val loginResult: Login
)