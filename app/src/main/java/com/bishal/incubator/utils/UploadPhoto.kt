package com.bishal.incubator.utils

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class UploadPhoto(val context: Context) {

    private var mPhotoUploadProgress : Double = 0.0

    fun uploadProfileImage(uri: Uri, folderName: String, callback: (String?)->Unit) {
        var imageUrl: String?
        FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
            .putFile(uri)
            .addOnSuccessListener { it ->
                it.storage.downloadUrl.addOnSuccessListener {
                    imageUrl = it.toString()
                    callback(imageUrl)
                }
            }
    }

    /*
    * upload the selected photo to firebase after conversion to smaller size
    * */
    fun uploadPhoto(imagePath: String, folderName: String, userId: String, callback: (Any?) -> Unit) {
        var imageUrl: String

        // convert the image url to bitmap
        val bitmap = ImageManager(context).getBitmap(imagePath = imagePath)
        val byteData = ImageManager(context).getBytesFromBitmap(bitmap, 75)

        FirebaseStorage.getInstance().getReference(folderName).child("$userId/${System.currentTimeMillis()}.jpg")
            .putBytes(byteData)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString()
                    callback(imageUrl)
                }
            }
            .addOnFailureListener {
                // Handle the failure
                callback(null)
            }
            .addOnProgressListener {
                val progress : Double = ((100 * it.bytesTransferred).toDouble() / (it.totalByteCount))
                if (progress - 15 > mPhotoUploadProgress) {
                    Toast.makeText(
                        context,
                        "photo upload progress : ${String.format("%.0f", progress)}%",
                        Toast.LENGTH_SHORT
                    ).show()
                    mPhotoUploadProgress = progress
                }
            }
    }
}


