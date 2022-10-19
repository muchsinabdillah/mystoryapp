package com.example.mystoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.data.model.StoryEntity
import com.example.mystoryapp.data.resource.CatalogueRepository

class GetStoryAllViewModel(catalogueRepository: CatalogueRepository) : ViewModel() {
    val story: LiveData<PagingData<StoryEntity>> =
        catalogueRepository.stories().cachedIn(viewModelScope)
}