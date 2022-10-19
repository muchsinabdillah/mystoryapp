package com.example.mystoryapp.data.resource.remote.response

import com.google.gson.annotations.SerializedName

class GetAddStoryResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String


)

