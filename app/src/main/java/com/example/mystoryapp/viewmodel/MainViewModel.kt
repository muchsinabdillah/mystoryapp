package com.example.mystoryapp.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.model.LoginEntity
import com.example.mystoryapp.data.model.StoryEntity
import com.example.mystoryapp.data.resource.CatalogueRepository
import com.example.mystoryapp.data.resource.remote.response.GetLoginResponse

class MainViewModel (private val catalogueRepository: CatalogueRepository) : ViewModel() {

    fun allstories(location: Int) = catalogueRepository.allstories(location)
    var datastory: LiveData<List<StoryEntity>> = catalogueRepository.datastory()

    var userlogin: LiveData<GetLoginResponse> = catalogueRepository.userlogin()

    val message: LiveData<String> = catalogueRepository.message()

    fun login(userlogin: LoginEntity) =
        catalogueRepository.login(userlogin)

}