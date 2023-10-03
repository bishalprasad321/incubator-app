package com.bishal.incubator

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bishal.incubator.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private val binding: ActivitySignInBinding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            super.onBackPressed()
        }
        binding.askCreateNowTextView.setOnClickListener{
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java).addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT))
        }

        binding.signInButton.setOnClickListener {
            startActivity(Intent(this@SignInActivity, HomeActivity::class.java).addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT))
        }
    }
}