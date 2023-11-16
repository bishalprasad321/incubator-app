package com.bishal.incubator.incubates_reels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bishal.incubator.databinding.FragmentIncubatesBinding


class IncubatesFragment : Fragment() {

    private val binding: FragmentIncubatesBinding by lazy {
        FragmentIncubatesBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}