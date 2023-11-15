package com.bishal.incubator.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bishal.incubator.AddPostActivity
import com.bishal.incubator.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private val binding: FragmentAddBinding by lazy {
        FragmentAddBinding.inflate(layoutInflater)
    }

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        checkPermissionRequired()
        return binding.root
    }

    private val permissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val isGranted = permissions.entries.all { it.value }
        if (isGranted) {
            startActivity(Intent(requireActivity(), AddPostActivity::class.java))
            activity?.finish()
        } else {
            showPermissionDialog()
        }
    }

    private fun showPermissionDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Permissions Required")
        builder.setMessage("Some permissions are needed to be allowed for this feature")
        builder.setPositiveButton("Grant") {d, _ ->
            d.cancel()
            startActivity(Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:${requireActivity().packageName}")
            ))
        }

        builder.setNegativeButton("Cancel") {d, _ ->
            d.dismiss()
        }
        builder.show()
    }

    private fun checkPermissionRequired() {
        if (hasPermission()) {
            startActivity(Intent(requireActivity(), AddPostActivity::class.java))
            activity?.finish()
        } else {
            permissionRequestLauncher.launch(permission)
        }
    }

    private fun hasPermission() : Boolean = permission.all {
        ActivityCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }
}