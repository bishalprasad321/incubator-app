package com.bishal.incubator.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bishal.incubator.models.Photo
import com.bishal.incubator.models.Posts
import com.bishal.incubator.utils.POSTS_NODE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyPostsViewModel : ViewModel() {
    private val _myPosts = MutableLiveData<List<Photo>>()
    val myPosts: LiveData<List<Photo>> get() = _myPosts

    fun fetchMyPosts(userId: String) {
        Firebase.firestore.collection(POSTS_NODE).document(userId)
            .get().addOnSuccessListener { postsDocument ->
                val postsData = postsDocument.toObject(Posts::class.java)
                if (postsData != null && !postsData.posts.isNullOrEmpty()) {
                    _myPosts.value = postsData.posts.orEmpty().reversed()
                }
            }
    }
}