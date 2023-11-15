@file:Suppress("DEPRECATION")

package com.bishal.incubator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bishal.incubator.databinding.ActivityMainBinding
import com.facebook.FacebookSdk

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FacebookSdk.sdkInitialize(this@MainActivity)

        binding.incubateButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, SignInActivity::class.java))
            finish()
        }
    }
}