package com.bishal.incubator.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bishal.incubator.adaptors.MyPostsImageAdapter
import com.bishal.incubator.databinding.FragmentMyPostBinding
import com.bishal.incubator.models.Photo
import com.bishal.incubator.viewmodels.MyPostsViewModel

class MyPostFragment : Fragment() {

    private val binding : FragmentMyPostBinding by lazy {
        FragmentMyPostBinding.inflate(layoutInflater)
    }

    private val viewModel: MyPostsViewModel by viewModels()

    private lateinit var myPosts : List<Photo>
    private lateinit var userId: String

    companion object {
        private const val USER_ID_KEY = "userId"

        fun newInstance(userId: String): MyPostFragment {
            val fragment = MyPostFragment()
            val args = Bundle()
            args.putString(USER_ID_KEY, userId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // initialization of variables
        myPosts = listOf()
        userId = arguments?.getString(USER_ID_KEY)!!

        binding.myPostsRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.myPosts.observe(viewLifecycleOwner) { myPosts ->
            // Update UI with the new data
            val adapter = MyPostsImageAdapter(myPosts)
            binding.myPostsRecyclerView.adapter = adapter
        }

        // Fetch data when the fragment is created or resumed
        viewModel.fetchMyPosts(userId)
    }
}