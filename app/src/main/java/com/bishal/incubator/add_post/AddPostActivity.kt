@file:Suppress("DEPRECATION")

package com.bishal.incubator.add_post

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import com.bishal.incubator.utils.allPermission


class AddPostActivity : AppCompatActivity() {

    private val binding : ActivityAddPostBinding by lazy {
        ActivityAddPostBinding.inflate(layoutInflater)
    }

    private lateinit var newPostViewPagerAdaptor: ViewPagerAdaptor

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
    *
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

    /*
    * Check if already has permission, if **not** then launch permission launcher
    * */
    private fun checkPermissionRequired() {
        if (hasPermission()) {
            Log.d("permissions", "All permissions granted")
        } else {
            permissionRequestLauncher.launch(allPermission)
        }
    }

    /*
    * Self check permission from the permissions array
    * */
    private fun hasPermission(): Boolean = allPermission.all {
        ActivityCompat.checkSelfPermission(
            this@AddPostActivity,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}