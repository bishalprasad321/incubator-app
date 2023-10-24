package com.bishal.incubator.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bishal.incubator.ChatActivity
import com.bishal.incubator.R
import com.bishal.incubator.SignInActivity
import com.bishal.incubator.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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

        // Google sign in options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Google sign in client
        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.signOutButton.setOnClickListener {
            googleSignInClient.signOut()
                .addOnCompleteListener(requireActivity()) {
                    if (it.isSuccessful) {
                        startActivity(Intent(requireActivity(), SignInActivity::class.java))
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            it.exception?.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            Firebase.auth.signOut()
        }

        return binding.root
    }
}