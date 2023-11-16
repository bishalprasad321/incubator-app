package com.bishal.incubator.add_post

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bishal.incubator.R
import com.bishal.incubator.databinding.FragmentPostNewPictureBinding
import com.bishal.incubator.utils.CAMERA_REQUEST_CODE

@Suppress("DEPRECATION")
class PostNewPictureFragment : Fragment() {

    private val binding: FragmentPostNewPictureBinding by lazy {
        FragmentPostNewPictureBinding.inflate(layoutInflater)
    }

    private var squareFit: Boolean = false

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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

        // close button on click
        binding.closeButton.setOnClickListener {
            if (binding.postImageView.drawable.constantState
                != requireContext().resources.getDrawable(R.drawable.splash).constantState) {
                val builder = AlertDialog.Builder(requireActivity(), R.style.MyDialogTheme)
                builder.setTitle("Discard Post?")
                builder.setMessage("Do you want to discard this post?")
                builder.setPositiveButton("Yes") { d, _ ->
                    d.dismiss()
                    activity?.finish()
                }

                builder.setNegativeButton("No") { d, _ ->
                    d.cancel()
                }
                builder.show()
            } else {
                activity?.finish()
            }
        }

        // camera button on click
        binding.actionCameraButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        // return ui
        return binding.root
    }

    /*
    * Start an intent for camera and save the taken image to image view
    * */
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity?.packageManager!!) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val image = data?.extras?.get("data") as Bitmap
            binding.postImageView.setImageBitmap(image)
        }
    }

    /*
    * init()
    * */

    private fun init() {

    }
}