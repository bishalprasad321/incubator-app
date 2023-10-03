package com.bishal.incubator

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bishal.incubator.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.askSignInTextView.setOnClickListener{
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java).addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT))
        }

        binding.backButton.setOnClickListener {
            super.onBackPressed()
        }
    }
}