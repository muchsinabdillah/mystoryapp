package com.example.mystoryapp.data.resource.remote.response

import com.example.mystoryapp.data.model.StoryEntity
import com.google.gson.annotations.SerializedName

class GetStoryResponse(
    @field:SerializedName("error")
    val error: String,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("listStory")
    val listStory: List<StoryEntity>
)