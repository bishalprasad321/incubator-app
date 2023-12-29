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
import com.google.firebase.auth.FirebaseAuth

class MyPostFragment : Fragment() {

    private val binding : FragmentMyPostBinding by lazy {
        FragmentMyPostBinding.inflate(layoutInflater)
    }

    private val viewModel: MyPostsViewModel by viewModels()

    private lateinit var myPosts : List<Photo>
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // initialization of variables
        myPosts = listOf()
        userId = FirebaseAuth.getInstance().currentUser!!.uid

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