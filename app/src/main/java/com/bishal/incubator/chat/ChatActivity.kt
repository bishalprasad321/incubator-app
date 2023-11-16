package com.bishal.incubator.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bishal.incubator.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private val binding : ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}