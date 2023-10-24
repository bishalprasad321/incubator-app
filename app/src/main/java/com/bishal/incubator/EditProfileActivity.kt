package com.bishal.incubator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bishal.incubator.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private val binding : ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.closeButton.setOnClickListener {
            finish()
        }

        binding.tickButton.setOnClickListener {
            finish()
        }
    }
}