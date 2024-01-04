package com.bishal.incubator.posts_content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bishal.incubator.databinding.FragmentPostsBinding

class Posts : Fragment() {

    private val binding : FragmentPostsBinding by lazy {
        FragmentPostsBinding.inflate(layoutInflater)
    }

    private lateinit var userId : String
    companion object {
        private const val USER_ID = "userId"

        fun newInstance(userId: String): Posts {
            val fragment = Posts()
            val args = Bundle()
            args.putString(USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getString(USER_ID)!!

    }
}