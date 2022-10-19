package com.example.mystoryapp.data.model

import com.google.gson.annotations.SerializedName

class Login(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("token")
    val token: String
)