package com.bishal.incubator

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bishal.incubator.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    private val binding: ActivitySignInBinding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.backButton.setOnClickListener {
            super.onBackPressed()
        }
        binding.askCreateNowTextView.setOnClickListener{
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java).addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT))
        }

        binding.signInButton.setOnClickListener {
            startActivity(Intent(this@SignInActivity, HomeActivity::class.java).addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT))
        }

        binding.signInButton.setOnClickListener {
            if ((binding.emailEtView.text.toString() == "") or
                (binding.passwordEtView.editText?.text.toString() == "")) {
                Toast.makeText(this@SignInActivity, "Please provide email and password", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(
                    binding.emailEtView.text.toString(), binding.passwordEtView.editText?.text.toString()
                ).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@SignInActivity, "Sign in successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SignInActivity, task.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}