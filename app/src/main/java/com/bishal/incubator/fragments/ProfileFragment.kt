package com.bishal.incubator.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bishal.incubator.EditProfileActivity
import com.bishal.incubator.R
import com.bishal.incubator.SettingsActivity
import com.bishal.incubator.adaptors.ViewPagerAdaptor
import com.bishal.incubator.databinding.FragmentProfileBinding
import com.bishal.incubator.models.User
import com.bishal.incubator.utils.USER_NODE
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    private lateinit var viewPagerAdaptor: ViewPagerAdaptor

    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setup tabs
        setUpTabs()

        // Edit profile button
        binding.editProfileButton.setOnClickListener{
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
        }

        // Settings Button
        binding.settingsButton.setOnClickListener {
            startActivity(Intent(requireActivity(), SettingsActivity::class.java))
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
            .get().addOnSuccessListener {
                val user: User = it.toObject<User>()!!
                binding.profileNameTextView.text = user.name
                binding.userNameAppBar.text = generateUserName(user.email)
                binding.profileUserBio.text = user.bio
                if (!user.image.isNullOrEmpty()) {
                    Picasso.get().load(user.image).into(binding.profileImageView)
                }
            }
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

    private fun generateUserName(email: String?) : String {
        var username = "@"
        for (it in email!!) {
            username += if (it == '@') {
                break
            } else if(it == '.') {
                '_'
            } else {
                it
            }
        }
        return username
    }
}