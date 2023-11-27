package com.bishal.incubator.add_post

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.bishal.incubator.R
import com.bishal.incubator.databinding.ActivityPostNextBinding
import com.bishal.incubator.models.User
import com.bishal.incubator.utils.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class PostNextActivity : AppCompatActivity() {

    private val binding : ActivityPostNextBinding by lazy {
        ActivityPostNextBinding.inflate(layoutInflater)
    }

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d("image path", intent.getStringExtra("selectedImage").toString())

        // initialize variables
        userId = FirebaseAuth.getInstance().currentUser!!.uid

        // setup firebase

        // fetch the post image from previous screen using intent get extra
        getSelectedImage()

        // back button on click listener
        binding.backButton.setOnClickListener {
            finish()
        }

        // Share button functionality
        binding.shareButton.setOnClickListener {
            // upload the image to the firebase

            // move to the home screen
        }
    }

    /*
    * on start activity
    * */
    override fun onStart() {
        super.onStart()
        fetchFirestore()
    }

    /*
    * gets the image from the incoming intent and displays the chosen image
    * */
    private fun getSelectedImage() {
        binding.postImage.load(intent.getStringExtra("selectedImage")) {
            placeholder(R.drawable.splash)
        }
    }

    /*
    * fetches the user image from the firestore and displays into the user image
    * */
    private fun fetchFirestore() {
        Firebase.firestore.collection(USER_NODE).document(userId)
            .get().addOnSuccessListener {
                val user: User = it.toObject<User>()!!
                if (!user.image.isNullOrEmpty()) {
                    binding.userImage.load(user.image) {
                        placeholder(R.drawable.user_filled)
                    }
                }
            }
    }
}