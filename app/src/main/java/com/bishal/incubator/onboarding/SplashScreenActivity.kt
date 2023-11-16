package com.bishal.incubator.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bishal.incubator.R
import com.bishal.incubator.home.HomeActivity
import com.bishal.incubator.sign_up_log_in.MainActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
            finish()
        }
    }
}