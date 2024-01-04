package com.bishal.incubator.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.bishal.incubator.R
import com.bishal.incubator.adaptors.ViewPagerAdaptor
import com.bishal.incubator.add_post.AddPostActivity
import com.bishal.incubator.databinding.FragmentProfileBinding
import com.bishal.incubator.settings.SettingsActivity
import com.bishal.incubator.viewmodels.ProfileDetailsViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment(){

    private lateinit var viewPagerAdaptor: ViewPagerAdaptor

    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private lateinit var currentUserId: String

    private val profileDetailsViewModel : ProfileDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // User id
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        // Edit profile button
        binding.editProfileButton.setOnClickListener{
             startActivity(
                 Intent(requireActivity(), EditProfileActivity::class.java)
                     .putExtra("userId", currentUserId)
                     .addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
             )
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

    override fun onStart() {
        super.onStart()
        // initialize the data and fetch the UI
        init()
        // Setup tabs
        setUpTabs()
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        // Fetch all profile fragment's related details
        profileDetailsViewModel.getUserProfileDetails(currentUserId)

        // follower count
        profileDetailsViewModel.followerCount.observe(viewLifecycleOwner) { followerCount ->
            binding.followersCount.text = followerCount
        }

        // following count
        profileDetailsViewModel.followingCount.observe(viewLifecycleOwner) { followingCount ->
            binding.followingCount.text = followingCount
        }

        // posts count
        profileDetailsViewModel.postCount.observe(viewLifecycleOwner) { postCount ->
            binding.postsCount.text = postCount
        }

        // load profile image
        profileDetailsViewModel.userProfileImagePath.observe(viewLifecycleOwner) { profileImagePath ->
            if (!profileImagePath.isNullOrEmpty()) {
                binding.profileImageView.load(profileImagePath) {
                    placeholder(R.drawable.user_filled)
                    error(R.drawable.splash)
                }
            }
        }

        // observe on user's name
        profileDetailsViewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.profileNameTextView.text = name
        }

        // observe on user's username
        profileDetailsViewModel.userUserName.observe(viewLifecycleOwner) { username ->
            binding.userNameAppBar.text = "@${username}"
        }

        // observe on user's bio
        profileDetailsViewModel.userBio.observe(viewLifecycleOwner) { bio ->
            binding.profileUserBio.text = bio
        }
    }

    /*
    * set up tab layout : My posts | My Incubates | My Bookmarks
    * */
    private fun setUpTabs() {
        viewPagerAdaptor = ViewPagerAdaptor(childFragmentManager)
        viewPagerAdaptor.addFragments(MyPostFragment.newInstance(currentUserId))
        viewPagerAdaptor.addFragments(MyIncubatesFragment())
        viewPagerAdaptor.addFragments(MyBookmarksFragment())
        binding.profileViewPager.adapter = viewPagerAdaptor
        binding.profileTabLayout.setupWithViewPager(binding.profileViewPager)

        binding.profileTabLayout.getTabAt(0)!!.setIcon(R.drawable.grid_view)
        binding.profileTabLayout.getTabAt(1)!!.setIcon(R.drawable.video_play_outlined)
        binding.profileTabLayout.getTabAt(2)!!.setIcon(R.drawable.bookmark)
    }
}