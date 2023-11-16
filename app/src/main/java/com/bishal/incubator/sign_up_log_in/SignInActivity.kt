package com.bishal.incubator.sign_up_log_in

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bishal.incubator.R
import com.bishal.incubator.databinding.ActivitySignInBinding
import com.bishal.incubator.home.HomeActivity
import com.bishal.incubator.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class SignInActivity : AppCompatActivity() {

    private val binding: ActivitySignInBinding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    // Intent launcher for google login
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount? = task.result
                val credentials = GoogleAuthProvider.getCredential(account?.idToken, null)
                auth.signInWithCredential(credentials).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this@SignInActivity, "Success google sign in", Toast.LENGTH_LONG).show()
                        binding.emailEtView.setText(FirebaseAuth.getInstance().currentUser?.email)
                        startActivity(Intent(this@SignInActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@SignInActivity, "Failed google sign in", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            Toast.makeText(this@SignInActivity, "Failed google sign in", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth

        // Google sign in options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Google sign in client
        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.backButton.setOnClickListener {
            super.onBackPressed()
        }
        binding.askCreateNowTextView.setOnClickListener{
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java)
                .addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT))
        }

        binding.signInButton.setOnClickListener {
            binding.signInProgressBar.visibility = View.VISIBLE
            if ((binding.emailEtView.text.toString() == "") or
                (binding.passwordEtView.editText?.text.toString() == "")) {
                Toast.makeText(this@SignInActivity, "Please provide email and password", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(
                    binding.emailEtView.text.toString(),
                    binding.passwordEtView.editText?.text.toString()
                )
                auth.signInWithEmailAndPassword(
                    user.email!!, user.password!!
                ).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        binding.signInProgressBar.visibility = View.GONE
                        Toast.makeText(this@SignInActivity, "Sign in successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignInActivity, HomeActivity::class.java).addFlags(
                            FLAG_ACTIVITY_REORDER_TO_FRONT))
                        finish()
                    } else {
                        Toast.makeText(this@SignInActivity, task.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                        binding.signInProgressBar.visibility =View.INVISIBLE
                    }
                }
            }
        }

        binding.googleButton.setOnClickListener {
            val signInClient = googleSignInClient.signInIntent
            launcher.launch(signInClient)
        }
    }
}