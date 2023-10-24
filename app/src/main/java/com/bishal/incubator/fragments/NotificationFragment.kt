package com.bishal.incubator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bishal.incubator.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {

    private val binding : FragmentNotificationBinding by lazy {
        FragmentNotificationBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.backButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        return binding.root
    }
}