package com.example.mystoryapp.ui.createstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.resource.CatalogueRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel (private val catalogueRepository: CatalogueRepository) : ViewModel() {

    fun upload(
        photo: MultipartBody.Part,
        des: RequestBody,
        lat: Double?,
        lng: Double?
    ) {
        catalogueRepository.upload(photo, des,  lat, lng)
    }

    val message: LiveData<String> = catalogueRepository.message()

}