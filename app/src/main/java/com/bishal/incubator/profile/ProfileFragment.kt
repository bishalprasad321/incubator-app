package com.bishal.incubator.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bishal.incubator.R
import com.bishal.incubator.settings.SettingsActivity
import com.bishal.incubator.adaptors.ViewPagerAdaptor
import com.bishal.incubator.databinding.FragmentProfileBinding
import com.bishal.incubator.models.User
import com.bishal.incubator.utils.FOLLOWER_NODE
import com.bishal.incubator.utils.FOLLOWING_NODE
import com.bishal.incubator.utils.POSTS_NODE
import com.bishal.incubator.utils.USER_NODE
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    private lateinit var viewPagerAdaptor: ViewPagerAdaptor

    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private lateinit var currentUserId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setup tabs
        setUpTabs()

        // User id
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        // Edit profile button
        binding.editProfileButton.setOnClickListener{
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java).putExtra("userId", currentUserId))
        }

        // Settings Button
        binding.settingsButton.setOnClickListener {
            startActivity(Intent(requireActivity(), SettingsActivity::class.java))
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        Firebase.firestore.collection(USER_NODE).document(currentUserId)
            .get().addOnSuccessListener {
                val user: User = it.toObject<User>()!!
                retrieveFollowersInfo()
                retrieveFollowingInfo()
                getUserPosts()
                binding.profileNameTextView.text = user.name
                binding.userNameAppBar.text = "@${user.username}"
                binding.profileUserBio.text = user.bio
                if (!user.image.isNullOrEmpty()) {
                    Glide.with(this@ProfileFragment).load(user.image)
                        .placeholder(R.drawable.user_filled).into(binding.profileImageView)
                }
            }
    }

    /*
    * get total number of followers
    * */
    private fun retrieveFollowersInfo() {
        Firebase.firestore.collection(FOLLOWER_NODE)
            .document(currentUserId).get().addOnSuccessListener { followersDocument ->
                val followersData = followersDocument.data
                val followersCount = (followersData?.get("followers") as List<*>).size
                binding.followersCount.text = followersCount.toString()
            }
    }

    /*
    * get total number of followings
    * */
    private fun retrieveFollowingInfo() {
        Firebase.firestore.collection(FOLLOWING_NODE)
            .document(currentUserId).get().addOnSuccessListener { followingDocument ->
                val followingData = followingDocument.data
                val followingCount = (followingData?.get("following") as List<*>).size
                binding.followingCount.text = followingCount.toString()
            }
    }

    /*
    * get total number of user's posts
    * */
    private fun getUserPosts() {
        Firebase.firestore.collection(POSTS_NODE)
            .document(currentUserId).get().addOnSuccessListener { postsDocument ->
                val postsData = postsDocument.data!!
                val postsCount = (postsData["posts"] as List<*>).size
                binding.postsCount.text = postsCount.toString()
            }
            .addOnFailureListener {
                Log.d("post count", it.localizedMessage!!)
            }
    }

    /*
    * set up tab layout : My posts | My Incubates | My Bookmarks
    * */
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
}