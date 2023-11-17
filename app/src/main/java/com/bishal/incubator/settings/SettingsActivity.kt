package com.bishal.incubator.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bishal.incubator.R
import com.bishal.incubator.databinding.ActivitySettingsBinding
import com.bishal.incubator.sign_up_log_in.SignInActivity
import com.bishal.incubator.utils.USER_NODE
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SettingsActivity : AppCompatActivity() {

    private val binding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }

    // Google sign in options
    private lateinit var gso: GoogleSignInOptions

    // Google sign in client
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance().currentUser!!
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this@SettingsActivity, gso)

        // Back button
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.signOutButton.setOnClickListener {
            signOut()
        }

        // Delete account button
        binding.deleteButton.setOnClickListener {

            val builder: AlertDialog.Builder = AlertDialog.Builder(this@SettingsActivity)
            builder
                .setMessage("Once deleted, account cannot be recovered!")
                .setTitle("Permanently delete your account?")
                .setPositiveButton("Delete") { dialog, _ ->
                    dialog.dismiss()
                    binding.settingsProgressBar.visibility = View.VISIBLE
                    user.delete().addOnCompleteListener {
                        if (it.isSuccessful) {
                            binding.settingsProgressBar.visibility = View.GONE
                            Firebase.firestore.collection(USER_NODE).document(user.uid).delete()
                            Toast.makeText(
                                this@SettingsActivity,
                                "Account deleted successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            googleSignInClient.signOut()
                            startActivity(Intent(this@SettingsActivity, SignInActivity::class.java))
                            finish()
                        } else {
                            binding.settingsProgressBar.visibility = View.INVISIBLE
                            Toast.makeText(
                                this@SettingsActivity,
                                it.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                .setNegativeButton("No"){ dialog, _ ->
                    dialog.dismiss()
                }

            builder.create().show()
        }
    }

    private fun signOut() {
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
        finish()
    }
}