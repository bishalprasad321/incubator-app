@file:Suppress("DEPRECATION")

package com.bishal.incubator

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bishal.incubator.databinding.ActivityAddPostBinding
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageOptions


class AddPostActivity : AppCompatActivity() {

    private val binding : ActivityAddPostBinding by lazy {
        ActivityAddPostBinding.inflate(layoutInflater)
    }

    private var squareFit: Boolean = false

    private val cropActivityResultLauncher =
        registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                val uriContent = result.uriContent
                val uriFilePath = result.getUriFilePath(this@AddPostActivity)
            } else {
                val exception = result.error
            }
        }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // Change fit type of the image
        binding.fitImageButton.setOnClickListener {
            if (squareFit) {
                binding.postImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                squareFit = !squareFit
            } else {
                binding.postImageView.scaleType = ImageView.ScaleType.FIT_CENTER
                squareFit = !squareFit
            }
        }

        binding.closeButton.setOnClickListener {
            onBackPressed()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(
            Intent(this@AddPostActivity, HomeActivity::class.java)
        )
        finish()
    }

    private fun startCrop() {
        val cropImageOptions: CropImageOptions = CropImageOptions()
        cropImageOptions.imageSourceIncludeGallery = true
    }
}