package com.bishal.incubator.utils

import android.net.Uri
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun uploadProfileImage(uri: Uri, folderName: String, callback: (String?)->Unit) {
    var imageUrl: String?
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener { it ->
            it.storage.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
                callback(imageUrl)
            }
        }
}

fun uploadPhoto(fileUri: Uri, folderName: String, userId: String, callback: (String?) -> Unit) {
    var imageUrl: String?
    // val file = File(fileUri.path!!)

    FirebaseStorage.getInstance().getReference(folderName).child("$userId/${System.currentTimeMillis()}.jpg")
        .putFile(fileUri)
        .addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                callback(imageUrl)
            }
        }
        .addOnFailureListener {
            // Handle the failure
            callback(null)
        }
}

interface PostCountCallback {
    fun onPostCountReceived(postCount: Int)
    fun onPostCountFailed(error: String)
}

fun getUserPostsCount(userId: String, callback: PostCountCallback) {
    Firebase.firestore.collection(POSTS_NODE).document(userId)
        .get().addOnSuccessListener {  postsDocument ->
            val postsData = postsDocument.data!!
            val postCount = (postsData["posts"] as List<*>).size
            callback.onPostCountReceived(postCount)
        }
        .addOnFailureListener {
            callback.onPostCountFailed(it.localizedMessage!!)
        }
}

interface FollowerCountCallback {
    fun onFollowerCountReceived(followerCount: Int)
    fun onFollowerCountFailed(error: String)
}

fun getUsersFollowerCount(userId: String, callback: FollowerCountCallback) {
    Firebase.firestore.collection(FOLLOWER_NODE)
        .document(userId).get().addOnSuccessListener { followersDocument ->
            val followersData = followersDocument.data
            val followersCount = (followersData?.get("followers") as List<*>).size
            callback.onFollowerCountReceived(followerCount = followersCount)
        }
        .addOnFailureListener {
            callback.onFollowerCountFailed(error = it.localizedMessage!!)
        }
}

interface FollowingCountCallback {
    fun onFollowingCountReceived(followingCount: Int)
    fun onFollowingCountFailed(error: String)
}

fun getUsersFollowingCount(userId: String, callback : FollowingCountCallback) {
    Firebase.firestore.collection(FOLLOWING_NODE)
        .document(userId).get().addOnSuccessListener { followingDocument ->
            val followingData = followingDocument.data
            val followingCount = (followingData?.get("following") as List<*>).size
            callback.onFollowingCountReceived(followingCount = followingCount)
        }
        .addOnFailureListener {
            callback.onFollowingCountFailed(error = it.localizedMessage!!)
        }
}


