package com.bishal.incubator.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bishal.incubator.models.Photo

class PostsViewModel : ViewModel() {
    private val _selectedPhoto = MutableLiveData<Photo>()
    val selectedPhoto : MutableLiveData<Photo>
        get() = _selectedPhoto

    fun fetchSelectedPhoto(photo : Photo) {
        _selectedPhoto.value = photo
    }
}