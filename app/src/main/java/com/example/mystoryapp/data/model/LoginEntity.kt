package com.example.mystoryapp.data.model

import com.google.gson.annotations.SerializedName

class LoginEntity(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)