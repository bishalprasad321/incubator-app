package com.bishal.incubator.posts_content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bishal.incubator.databinding.FragmentPostsBinding

class Posts : Fragment() {

    private val binding : FragmentPostsBinding by lazy {
        FragmentPostsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return binding.root
    }
}