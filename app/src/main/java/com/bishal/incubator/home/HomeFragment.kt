package com.bishal.incubator.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bishal.incubator.chat.ChatActivity
import com.bishal.incubator.R
import com.bishal.incubator.databinding.FragmentHomeBinding
import com.bishal.incubator.notification.NotificationFragment

class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Notification button
        binding.notificationButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.add(
                NotificationFragment(),
                ""
            )?.addToBackStack(null)?.replace(
                R.id.navHostFragment,
                NotificationFragment()
            )?.commit()
        }

        // Chats Button
        binding.chatButton.setOnClickListener {
            startActivity(Intent(requireActivity(), ChatActivity::class.java))
        }

        return binding.root
    }
}