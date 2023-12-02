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
import com.bishal.incubator.adaptors.ViewPagerAdaptor
import com.bishal.incubator.add_post.AddPostActivity
import com.bishal.incubator.databinding.FragmentProfileBinding
import com.bishal.incubator.models.User
import com.bishal.incubator.settings.SettingsActivity
import com.bishal.incubator.utils.FirebaseMethods
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
        // User id
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        // Edit profile button
        binding.editProfileButton.setOnClickListener{
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java).putExtra("userId", currentUserId))
        }

        // add post button
        binding.addPostButton.setOnClickListener{
            startActivity(Intent(requireActivity(), AddPostActivity::class.java))
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
                // fetch and show user's posts count
                FirebaseMethods(requireContext()).getUserPostsCount(currentUserId, object:
                    FirebaseMethods.PostCountCallback {
                    override fun onPostCountReceived(postCount: Int) {
                        Log.d("Post Count", postCount.toString())
                        binding.postsCount.text = postCount.toString()
                    }
                    override fun onPostCountFailed(error: String) {
                        Log.d("Post Count", error)
                    }
                })

                // fetch and show user's follower count
                FirebaseMethods(requireContext()).getUsersFollowerCount(currentUserId, object: FirebaseMethods.FollowerCountCallback {
                    override fun onFollowerCountReceived(followerCount: Int) {
                        binding.followersCount.text = followerCount.toString()
                        Log.d("Follower Count", followerCount.toString())
                    }
                    override fun onFollowerCountFailed(error: String) {
                        Log.d("Follower Count", error)
                    }
                })

                // fetch and show user's following count
                FirebaseMethods(requireContext()).getUsersFollowingCount(currentUserId, object :
                    FirebaseMethods.FollowingCountCallback {
                    override fun onFollowingCountReceived(followingCount: Int) {
                        binding.followingCount.text = followingCount.toString()
                        Log.d("Following Count", followingCount.toString())
                    }
                    override fun onFollowingCountFailed(error: String) {
                        Log.d("Following Count", error)
                    }
                })

                // fetch and show user's basic details
                binding.profileNameTextView.text = user.name
                binding.userNameAppBar.text = "@${user.username}"
                binding.profileUserBio.text = user.bio
                if (!user.image.isNullOrEmpty()) {
                    Glide.with(this@ProfileFragment).load(user.image)
                        .placeholder(R.drawable.user_filled).into(binding.profileImageView)
                }
            }

        // Setup tabs
        setUpTabs()
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