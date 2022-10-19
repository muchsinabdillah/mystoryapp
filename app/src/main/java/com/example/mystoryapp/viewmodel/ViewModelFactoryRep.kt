package com.example.mystoryapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.data.resource.CatalogueRepository
import com.example.mystoryapp.di.Injection
import com.example.mystoryapp.ui.createstory.AddStoryViewModel
import com.example.mystoryapp.ui.signup.RegisterViewModel

class ViewModelFactoryRep private constructor
    (
    private val catalogueRepository: CatalogueRepository
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: ViewModelFactoryRep? = null
        fun getInstance(context: Context): ViewModelFactoryRep =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactoryRep(Injection.provideRepository(context))
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GetStoryAllViewModel::class.java) -> {
                GetStoryAllViewModel(catalogueRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(catalogueRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(catalogueRepository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(catalogueRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}