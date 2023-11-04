package com.bishal.incubator.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bishal.incubator.R
import com.bishal.incubator.adaptors.ViewPagerAdaptor
import com.bishal.incubator.databinding.FragmentUserProfileBinding
import com.bishal.incubator.models.User
import com.bishal.incubator.utils.FOLLOWER_NODE
import com.bishal.incubator.utils.FOLLOWING_NODE
import com.bishal.incubator.utils.USER_NODE
import com.bishal.incubator.utils.generateDisplayUsername
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class UserProfileFragment : Fragment() {

    private val binding: FragmentUserProfileBinding by lazy {
        FragmentUserProfileBinding.inflate(layoutInflater)
    }

    private lateinit var viewPagerAdaptor: ViewPagerAdaptor
    private lateinit var userId: String
    private lateinit var currentUser: String

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // user id from the search fragment
        userId = arguments?.getString("userId").toString()
        Log.d("userId", userId)

        // Initialization of lateinit variables
        currentUser = FirebaseAuth.getInstance().currentUser!!.uid

        // Follow button click listener
        binding.followProfileButton.setOnClickListener {
            if (binding.followProfileButton.text.toString() == "Follow") {
                // Follow action
                binding.followProfileButton.text = "Following"
                binding.followProfileButton.setBackgroundResource(R.drawable.profile_button_gradient_outline_bg)

                // Add searched user to the following list of current user's account
                com.google.firebase.Firebase.firestore.collection(FOLLOWING_NODE)
                    .document(currentUser)
                    .update("following", FieldValue.arrayUnion(userId))

                // Add the current user to the follower list of searched user's account
                com.google.firebase.Firebase.firestore.collection(FOLLOWER_NODE)
                    .document(userId)
                    .update("followers", FieldValue.arrayUnion(currentUser))
            } else {
                // Unfollow action
                binding.followProfileButton.text = "Follow"
                binding.followProfileButton.setBackgroundResource(R.drawable.profile_gradient_button_bg)

                // Remove the searched user from the following list of the current user's account
                com.google.firebase.Firebase.firestore.collection(FOLLOWING_NODE).document(currentUser)
                    .update("following", FieldValue.arrayRemove(userId))

                // Remove the current user from the follower's list of the searched user's account
                com.google.firebase.Firebase.firestore.collection(FOLLOWER_NODE).document(userId)
                    .update("followers", FieldValue.arrayRemove(currentUser))
            }
        }

        // set up tabs
        setUpTabs()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        retrieveProfileInfo(userId)
    }

    private fun setUpTabs() {
        viewPagerAdaptor = ViewPagerAdaptor(requireActivity().supportFragmentManager)
        viewPagerAdaptor.addFragments(MyPostFragment())
        viewPagerAdaptor.addFragments(MyIncubatesFragment())
        viewPagerAdaptor.addFragments(MyBookmarksFragment())
        binding.profileViewPager.adapter = viewPagerAdaptor
        binding.profileTabLayout.setupWithViewPager(binding.profileViewPager)

        binding.profileTabLayout.getTabAt(0)!!.setIcon(R.drawable.grid_view)
        binding.profileTabLayout.getTabAt(1)!!.setIcon(R.drawable.video_play_outlined)
        binding.profileTabLayout.getTabAt(2)!!.setIcon(R.drawable.bookmark)
    }

    private fun retrieveProfileInfo(userId: String) {
        Firebase.firestore.collection(USER_NODE).document(userId)
            .get().addOnSuccessListener {
                val user: User = it.toObject<User>()!!
                retrieveFollowInfo(userId)
                binding.profileNameTextView.text = user.name
                binding.userNameAppBar.text = generateDisplayUsername(user.email)
                binding.profileUserBio.text = user.bio
                if (!user.image.isNullOrEmpty()){
                    Picasso.get().load(user.image).into(binding.profileImageView)
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun retrieveFollowInfo(userId: String) {
        // Get references to the Firestore collections
        val followingRef = com.google.firebase.Firebase.firestore.collection(FOLLOWING_NODE)
            .document(FirebaseAuth.getInstance().currentUser!!.uid)

        // Check if the current user is following the searched user
        followingRef.get().addOnSuccessListener { followingDocument ->
            val followingData = followingDocument.data
            if (followingData != null && userId in followingData["following"] as List<*>) {
                // The current user is following the searched user
                binding.followProfileButton.text = "Following"
                binding.followProfileButton.setBackgroundResource(R.drawable.profile_button_gradient_outline_bg)
            } else {
                // The current user is not following the searched user
                binding.followProfileButton.text = "Follow"
                binding.followProfileButton.setBackgroundResource(R.drawable.profile_gradient_button_bg)
            }
        }
    }

    companion object {
        fun newInstance(userId: String): UserProfileFragment {
            val fragment = UserProfileFragment()
            val args = Bundle()
            args.putString("userId", userId)
            fragment.arguments = args
            return fragment
        }
    }
}