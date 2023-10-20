package com.bishal.incubator.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bishal.incubator.R
import com.bishal.incubator.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private val binding: FragmentAddBinding by lazy {
        FragmentAddBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}