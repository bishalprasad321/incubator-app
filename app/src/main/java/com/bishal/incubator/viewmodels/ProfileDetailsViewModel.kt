package com.bishal.incubator.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bishal.incubator.models.User
import com.bishal.incubator.utils.FirebaseFetchMethods
import com.bishal.incubator.utils.USER_NODE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class ProfileDetailsViewModel : ViewModel() {
    private val _userProfileImagePath = MutableLiveData<String?>()
    private val _userName = MutableLiveData<String?>()
    private val _userUserName = MutableLiveData<String?>()
    private val _userBio = MutableLiveData<String?>()
    private val _postsCount = MutableLiveData<String?>()
    private val _followerCount = MutableLiveData<String?>()
    private val _followingCount = MutableLiveData<String?>()

    val userProfileImagePath : MutableLiveData<String?> get() = _userProfileImagePath
    val userName : MutableLiveData<String?> get() = _userName
    val userUserName : MutableLiveData<String?> get() = _userUserName
    val userBio : MutableLiveData<String?> get() = _userBio
    val postCount : MutableLiveData<String?> get() = _postsCount
    val followerCount : MutableLiveData<String?> get() = _followerCount
    val followingCount : MutableLiveData<String?> get() = _followingCount

    fun getUserProfileDetails(userId: String) {
        Firebase.firestore.collection(USER_NODE).document(userId)
            .get().addOnSuccessListener {
                val user = it.toObject<User>()
                // fetch the user's basic details
                _userProfileImagePath.value = user?.image!!
                _userName.value = user.name!!
                _userUserName.value = user.username!!
                _userBio.value = user.bio!!

                // fetch and show user's posts count
                FirebaseFetchMethods().getUserPostsCount(userId, object:
                    FirebaseFetchMethods.PostCountCallback {
                    override fun onPostCountReceived(postCount: Int) {
                        Log.d("Post Count", postCount.toString())
                        _postsCount.value = postCount.toString()
                    }
                    override fun onPostCountFailed(error: String) {
                        Log.d("Post Count", error)
                    }
                })

                // fetch and show user's follower count
                FirebaseFetchMethods().getUsersFollowerCount(
                    userId, object: FirebaseFetchMethods.FollowerCountCallback {
                        override fun onFollowerCountReceived(followerCount: Int) {
                            Log.d("Follower Count", followerCount.toString())
                            _followerCount.value = followerCount.toString()
                        }
                        override fun onFollowerCountFailed(error: String) {
                            Log.d("Follower Count", error)
                        }
                    })

                // fetch and show user's following count
                FirebaseFetchMethods().getUsersFollowingCount(userId, object :
                    FirebaseFetchMethods.FollowingCountCallback {
                    override fun onFollowingCountReceived(followingCount: Int) {
                        Log.d("Following Count", followingCount.toString())
                        _followingCount.value = followingCount.toString()
                    }
                    override fun onFollowingCountFailed(error: String) {
                        Log.d("Following Count", error)
                    }
                })
            }
    }
}