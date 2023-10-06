package com.bishal.incubator

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bishal.incubator.databinding.ActivityHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeActivity : AppCompatActivity() {

    // Google sign in options
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    // initialisation of lateinit variables
    private val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /*binding.signOutButton.setOnClickListener {
            googleSignInClient.signOut()
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        startActivity(Intent(this@HomeActivity, SignInActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@HomeActivity,
                            it.exception?.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            Firebase.auth.signOut()
        }*/
    }
}