package com.bishal.incubator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bishal.incubator.databinding.ActivityEditProfileBinding
import com.bishal.incubator.utils.USER_NODE
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class EditProfileActivity : AppCompatActivity() {

    private val binding : ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    private lateinit var profileImage: String
    private lateinit var userName: String
    private lateinit var userBio: String
    private lateinit var profileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Firebase.firestore.collection(USER_NODE).document()

        binding.profileImageView.setImageURI(FirebaseAuth.getInstance().currentUser!!.photoUrl)
        binding.userNameEtView.setText("")


        binding.closeButton.setOnClickListener {
            finish()
        }

        binding.tickButton.setOnClickListener {
            finish()
        }
    }
}