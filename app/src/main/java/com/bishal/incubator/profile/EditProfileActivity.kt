package com.bishal.incubator.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bishal.incubator.R
import com.bishal.incubator.databinding.ActivityEditProfileBinding
import com.bishal.incubator.home.HomeActivity
import com.bishal.incubator.models.User
import com.bishal.incubator.utils.USER_NODE
import com.bishal.incubator.utils.USER_PROFILE_FOLDER
import com.bishal.incubator.utils.uploadImage
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class EditProfileActivity : AppCompatActivity() {

    private val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    private lateinit var userId: String
    private var profileName: String = ""
    private var profileUserName: String = ""
    private var profileBio: String = ""
    private var profileImage: String = ""
    private lateinit var updatedUserDetails: User

    // Gallery Launcher for editing profile image
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uploadImage(uri, USER_PROFILE_FOLDER) {
                    if (it == null) {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Failed to load image",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        profileImage = it
                        binding.profileImageView.setImageURI(uri)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // User id
        userId = intent.getStringExtra("userId").toString()

        // Updated user details instance
        updatedUserDetails = User()

        // Load the previously stored current user's data
        binding.loadingDataProgressBar.visibility = View.VISIBLE
        Firebase.firestore.collection(USER_NODE).document(userId)
            .get().addOnSuccessListener {
                val user: User = it.toObject<User>()!!
                binding.userNameEtView.setText(user.username)
                binding.nameEtView.setText(user.name)
                binding.bioTextView.setText(user.bio)
                Glide.with(this@EditProfileActivity).load(user.image)
                    .placeholder(R.drawable.user_filled).into(binding.profileImageView)
                profileImage = user.image!!
                binding.loadingDataProgressBar.visibility = View.GONE

                // get non-changeable data
                updatedUserDetails.password = user.password
                updatedUserDetails.email = user.email
            }
            .addOnFailureListener {
                binding.loadingDataProgressBar.visibility = View.INVISIBLE
                Toast.makeText(this@EditProfileActivity, it.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }

        // On click listener for (profile image) edit avatar option
        binding.changeAvatarTextView.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.closeButton.setOnClickListener {
            finish()
        }

        binding.tickButton.setOnClickListener {
            // Get the updated fields into the variables
            profileName = binding.nameEtView.text.toString()
            profileUserName = binding.userNameEtView.text.toString()
            profileBio = binding.bioTextView.text.toString()

            // Load the updated data to user's details
            updatedUserDetails.image = profileImage
            updatedUserDetails.username = profileUserName
            updatedUserDetails.name = profileName
            updatedUserDetails.bio = profileBio
            updatedUserDetails.lowercaseName = profileName.lowercase()

            saveUpdatedUserInfo(userId, updatedUserDetails)
        }
    }

    private fun saveUpdatedUserInfo(userId: String, updatedUserDetails: User) {
        binding.loadingDataProgressBar.visibility = View.VISIBLE
        Firebase.firestore.collection(USER_NODE).document(userId)
            .set(updatedUserDetails, SetOptions.merge())
            .addOnSuccessListener {
                binding.loadingDataProgressBar.visibility = View.GONE
                Toast.makeText(
                    this@EditProfileActivity,
                    "Profile updated successfully!!",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@EditProfileActivity, HomeActivity::class.java))
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@EditProfileActivity, it.localizedMessage, Toast.LENGTH_LONG)
                    .show()
                binding.loadingDataProgressBar.visibility = View.INVISIBLE
            }
    }
}