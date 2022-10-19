package com.example.mystoryapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.model.RegisterEntity
import com.example.mystoryapp.data.resource.CatalogueRepository

class RegisterViewModel(private val catalogueRepository: CatalogueRepository) : ViewModel() {

    fun register(userregister: RegisterEntity) =
        catalogueRepository.register(userregister)

    val message: LiveData<String> = catalogueRepository.message()

}