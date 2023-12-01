package com.bishal.incubator.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bishal.incubator.adaptors.MyPostsImageAdapter
import com.bishal.incubator.databinding.FragmentMyPostBinding
import com.bishal.incubator.models.Photo
import com.bishal.incubator.models.Posts
import com.bishal.incubator.utils.POSTS_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyPostFragment : Fragment() {

    private val binding : FragmentMyPostBinding by lazy {
        FragmentMyPostBinding.inflate(layoutInflater)
    }

    private lateinit var myPosts : List<Photo>
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // initialization of variables
        myPosts = listOf()
        userId = FirebaseAuth.getInstance().currentUser!!.uid

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        // set the grid layout manager to the recycler view
        binding.myPostsRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        // fetch the photos from the firebase
        Firebase.firestore.collection(POSTS_NODE).document(userId)
            .get().addOnSuccessListener { postsDocument ->
                val postsData = postsDocument.toObject(Posts::class.java)
                if (postsData != null) {
                    myPosts = postsData.posts.orEmpty()
                    for (post in myPosts) run {
                        Log.d("Posts", post.imagePath!!)
                    }
                    val adapter = MyPostsImageAdapter(myPosts)
                    binding.myPostsRecyclerView.adapter = adapter
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load : ${it.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

}