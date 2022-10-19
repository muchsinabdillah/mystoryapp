package com.example.mystoryapp.data.model

import com.google.gson.annotations.SerializedName

class RegisterEntity(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)