package com.bishal.incubator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bishal.incubator.databinding.ActivitySettingsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsActivity : AppCompatActivity() {

    private val binding : ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Back button
        binding.backButton.setOnClickListener {
            finish()
        }

        // Google sign in options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Google sign in client
        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this@SettingsActivity, gso)

        binding.signOutButton.setOnClickListener {
            googleSignInClient.signOut()
                .addOnCompleteListener(this@SettingsActivity) {
                    if (it.isSuccessful) {
                        startActivity(Intent(this@SettingsActivity, SignInActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@SettingsActivity,
                            it.exception?.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            Firebase.auth.signOut()
        }
    }
}