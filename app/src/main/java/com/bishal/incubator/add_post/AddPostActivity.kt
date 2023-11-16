@file:Suppress("DEPRECATION")

package com.bishal.incubator.add_post

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bishal.incubator.adaptors.ViewPagerAdaptor
import com.bishal.incubator.databinding.ActivityAddPostBinding
import com.bishal.incubator.home.HomeActivity


class AddPostActivity : AppCompatActivity() {

    private val binding : ActivityAddPostBinding by lazy {
        ActivityAddPostBinding.inflate(layoutInflater)
    }

    private lateinit var newPostViewPagerAdaptor: ViewPagerAdaptor

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // check for the permissions first
        checkPermissionRequired()

        // set up viewpager tabs
        setupViewPager()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(
            Intent(this@AddPostActivity, HomeActivity::class.java)
        )
        finish()
    }

    /*
    * Setup viewpager
    * */
    private fun setupViewPager() {
        newPostViewPagerAdaptor= ViewPagerAdaptor(this@AddPostActivity.supportFragmentManager)
        newPostViewPagerAdaptor.addFragments(PostNewPictureFragment())
        newPostViewPagerAdaptor.addFragments(PostNewIncubatesFragment())
        newPostViewPagerAdaptor.addFragments(PostNewStoriesFragment())

        binding.newPostViewPager.adapter = newPostViewPagerAdaptor
        binding.postCategoryTab.setupWithViewPager(binding.newPostViewPager)

        binding.postCategoryTab.getTabAt(0)!!.text = "Post"
        binding.postCategoryTab.getTabAt(1)!!.text = "Incubates"
        binding.postCategoryTab.getTabAt(2)!!.text = "Stories"
    }

    /*
    * Checking permissions
    * Handle the latest versions of android for permissions
    * */
    private val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    /*
    * permission launcher
    * */
    private val permissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.entries.all { it.value }
        if (isGranted) {
            Toast.makeText(this@AddPostActivity, "Start your new post", Toast.LENGTH_SHORT).show()
        } else {
            showPermissionDialog()
        }
    }

    /*
    * Permissions dialog, if all or some permissions not granted
    * */
    private fun showPermissionDialog() {
        val builder = AlertDialog.Builder(this@AddPostActivity)
        builder.setTitle("Permissions Required")
        builder.setMessage("Some permissions are needed to be allowed for this feature")
        builder.setPositiveButton("Grant") { d, _ ->
            d.cancel()
            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${this.packageName}")
                )
            )
        }

        builder.setNegativeButton("Cancel") { d, _ ->
            d.dismiss()
        }
        builder.show()
    }

    private fun checkPermissionRequired() {
        if (hasPermission()) {
            Log.d("permissions", "All permissions granted")
        } else {
            permissionRequestLauncher.launch(permission)
        }
    }

    private fun hasPermission(): Boolean = permission.all {
        ActivityCompat.checkSelfPermission(
            this@AddPostActivity,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}