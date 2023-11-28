package com.bishal.incubator.add_post

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.bishal.incubator.R
import com.bishal.incubator.databinding.ActivityPostNextBinding
import com.bishal.incubator.models.Photo
import com.bishal.incubator.models.Posts
import com.bishal.incubator.models.User
import com.bishal.incubator.utils.POSTS_NODE
import com.bishal.incubator.utils.PostCountCallback
import com.bishal.incubator.utils.USER_NODE
import com.bishal.incubator.utils.USER_PHOTO_FOLDER
import com.bishal.incubator.utils.getUserPostsCount
import com.bishal.incubator.utils.uploadPhoto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.io.File
import java.util.Calendar

@Suppress("DEPRECATION")
class PostNextActivity : AppCompatActivity() {

    private val binding : ActivityPostNextBinding by lazy {
        ActivityPostNextBinding.inflate(layoutInflater)
    }

    private lateinit var selectedPost: String
    private var postsCount: Int = 0
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d("image path", intent.getStringExtra("selectedImage").toString())

        // initialize variables
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        selectedPost = intent.getStringExtra("selectedImage").toString()
        getUserPostsCount(userId, object : PostCountCallback {
            override fun onPostCountReceived(postCount: Int) {
                postsCount = postCount
            }
            override fun onPostCountFailed(error: String) {
                Log.d("Posts Count", error)
            }
        })

        // setup firebase

        // fetch the post image from previous screen using intent get extra
        getSelectedImage()

        // back button on click listener
        binding.backButton.setOnClickListener {
            finish()
        }

        // Share button functionality
        binding.shareButton.setOnClickListener {
            binding.newPostProgressBar.visibility = View.VISIBLE
            // upload the image to the storage
            addPostToFireStore(userId, binding.captionEditText.text.toString(), (postsCount + 1).toString(), null)

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
        binding.postImage.load(selectedPost) {
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

    /*
    * Add selected post to firestore
    * */
    private fun addPostToFireStore(userId: String, caption : String?, postId: String, tags: String?) {
        uploadPhoto(Uri.fromFile(File(selectedPost)), USER_PHOTO_FOLDER, userId) { downloadImageUrl ->
            if (downloadImageUrl != null) {
                val photo = Photo(
                    imagePath = downloadImageUrl,
                    dateCreated = Calendar.getInstance().time.toString(),
                    caption = caption,
                    postId = postId,
                    tags = tags
                )

                Firebase.firestore.collection(POSTS_NODE).document(userId)
                    .get().addOnSuccessListener { postsDocument ->
                        val postsData = postsDocument.toObject(Posts::class.java)
                        if (postsData != null) {
                            val currentPhotos = postsData.posts.orEmpty().toMutableList()
                            currentPhotos.add(photo)

                            // Update the Firestore document with the updated list of photos
                            Firebase.firestore.collection(POSTS_NODE).document(userId)
                                .update("posts", currentPhotos)
                                .addOnSuccessListener {
                                    Log.d("AddPhoto", "Photo added successfully.")
                                    Toast.makeText(
                                        this@PostNextActivity,
                                        "Post uploaded to the storage",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.newPostProgressBar.visibility = View.GONE
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(
                                        this@PostNextActivity,
                                        exception.localizedMessage,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                    }
                    .addOnFailureListener{
                        binding.newPostProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(this@PostNextActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}