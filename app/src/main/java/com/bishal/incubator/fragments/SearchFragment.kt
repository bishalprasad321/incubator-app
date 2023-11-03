package com.bishal.incubator.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bishal.incubator.adaptors.UserAdaptor
import com.bishal.incubator.databinding.FragmentSearchBinding
import com.bishal.incubator.models.Users
import com.bishal.incubator.utils.USER_NODE
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private val binding: FragmentSearchBinding by lazy {
        FragmentSearchBinding.inflate(layoutInflater)
    }

    private var mUserList: MutableList<Users>? = null
    private var userAdaptor: UserAdaptor? = null
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.searchRecyclerView.setHasFixedSize(true)
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mUserList = mutableListOf()
        userAdaptor = context?.let {
            UserAdaptor(it, mUserList as MutableList<Users>, true)
        }

        binding.searchRecyclerView.adapter = userAdaptor

        binding.searchBarEtView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchQuery = binding.searchBarEtView.text?.toString()?.lowercase()
                if (searchQuery!!.isEmpty()) {
                    // Clear the users list and update ui accordingly
                    mUserList?.clear()
                    userAdaptor?.notifyDataSetChanged()
                } else {
                    // cancel previous search
                    searchJob?.cancel()

                    searchJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(500)
                        retrieveUsers(searchQuery)
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveUsers(searchQuery: String) {
        val userCollection = Firebase.firestore.collection(USER_NODE)
        val usernameQuery = userCollection.whereEqualTo("username", searchQuery)
        val nameQuery = userCollection.whereEqualTo("lowercaseName", searchQuery)

        mUserList?.clear()
        userAdaptor?.notifyDataSetChanged()

        // Search Query
        usernameQuery.get().addOnSuccessListener { usernameDocuments ->
            for (document in usernameDocuments) {
                val user = document.toObject(Users::class.java)
                mUserList?.add(user)
                Log.d("User", user.toString())
            }

            // Name Query
            nameQuery.get().addOnSuccessListener { nameDocuments ->
                for (document in nameDocuments) {
                    val user = document.toObject(Users::class.java)
                    if (!(mUserList?.contains(user)!!)) {
                        mUserList?.add(user)
                        Log.d("User", user.toString())
                    }
                }
                userAdaptor?.notifyDataSetChanged()
            }.addOnFailureListener {  exception ->
                Toast.makeText(requireContext(), exception.localizedMessage?.toString(), Toast.LENGTH_LONG).show()
            }
        }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage?.toString(), Toast.LENGTH_LONG).show()
            }
    }
}