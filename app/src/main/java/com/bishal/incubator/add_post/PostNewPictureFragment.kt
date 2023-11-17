package com.bishal.incubator.add_post

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.bishal.incubator.R
import com.bishal.incubator.adaptors.ImageAdapter
import com.bishal.incubator.databinding.FragmentPostNewPictureBinding
import com.bishal.incubator.utils.CAMERA_REQUEST_CODE
import com.bishal.incubator.utils.FilePaths
import com.bishal.incubator.utils.FileSearch

@Suppress("DEPRECATION")
class PostNewPictureFragment : Fragment(), ImageAdapter.OnImageItemClickListener {

    private val binding: FragmentPostNewPictureBinding by lazy {
        FragmentPostNewPictureBinding.inflate(layoutInflater)
    }

    private var squareFit: Boolean = false

    private lateinit var directories : ArrayList<String>

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // lateinit vars initialization
        directories = arrayListOf()

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

        // call init()
        init()

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

    /*
    * on activity result for taking pictures from camera and putting into the image view
    * */
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
        val filePaths = FilePaths()

        // check for other folders inside "/storage/emulated/0/pictures"
        if (FileSearch().getDirectoryPaths(filePaths.PICTURES).isNotEmpty()) {
            directories = FileSearch().getDirectoryPaths(filePaths.PICTURES)
        }
        directories.add(filePaths.CAMERA)

        val simplifiedDirectories = convertToRelativeDirectory(directories)

        val pathAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, simplifiedDirectories)
        pathAdapter.setDropDownViewResource(R.layout.spinner_dropdown_design)
        binding.imagePathSpinner.adapter = pathAdapter
        binding.imagePathSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(requireContext(), "Selected : ${simplifiedDirectories[position]}", Toast.LENGTH_SHORT).show()
                // setup recycler view with this directory
                setupRecyclerView(directories[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected (if needed)
            }
        }
    }

    /*
    * setup recycler view
    * */
    @SuppressLint("NotifyDataSetChanged")
    private fun setupRecyclerView(selectedDirectory : String) {
        val imageUrl: ArrayList<String> = FileSearch().getFilePaths(selectedDirectory)

        for (image in imageUrl) {
            Log.d("image", image)
        }

        val galleryAdapter = ImageAdapter(imageUrl)
        galleryAdapter.setOnImageItemClickListener(this@PostNewPictureFragment)
        binding.galleryRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        binding.galleryRecyclerView.adapter = galleryAdapter
        galleryAdapter.notifyDataSetChanged()
    }

    /*
    * Convert the absolute path to directory name for the users
    * */
    private fun convertToRelativeDirectory(directories: ArrayList<String>) : ArrayList<String> {
        val simplifiedDirectories : ArrayList<String> = arrayListOf()
        for (directory in directories) {
            var folderName = ""
            for (i in directory.indices.reversed()) {
                if (directory[i] != '/') {
                    folderName += directory[i]
                } else {
                    break
                }
            }
            simplifiedDirectories.add(folderName.reversed())
        }
        return simplifiedDirectories
    }


    /*
    * Set the selected image to the post image view, for **new post**
    * */
    override fun onImageItemClick(imagePath: String) {
        binding.postImageView.load(imagePath) {
            placeholder(R.drawable.splash)
        }
    }
}