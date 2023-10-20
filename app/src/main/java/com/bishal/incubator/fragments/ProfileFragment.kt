package com.bishal.incubator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        viewPagerAdaptor = ViewPagerAdaptor(requireActivity().supportFragmentManager)
        viewPagerAdaptor.addFragments(MyPostFragment(), "My Posts")
        viewPagerAdaptor.addFragments(MyIncubatesFragment(), "My Incubates")

        binding.profileViewPager.adapter = viewPagerAdaptor
        binding.profileTabLayout.setupWithViewPager(binding.profileViewPager)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
            .get().addOnSuccessListener {
                val user: User = it.toObject<User>()!!
                binding.profileNameTextView.text = user.name
                binding.profileUserNameTextView.text = generateUserName(user.email)
                if (!user.image.isNullOrEmpty()) {
                    Picasso.get().load(user.image).into(binding.profileImageView)
                }
            }
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