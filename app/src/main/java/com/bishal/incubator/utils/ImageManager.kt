package com.bishal.incubator.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ImageManager(val context: Context) {

    /*
    * convert the image url to bitmap
    * */
    fun getBitmap(imagePath: String) : Bitmap {
        val imageFile = File(imagePath)
        var fileInputStream : FileInputStream? = null
        var bitmap : Bitmap? = null
        try {
            fileInputStream = FileInputStream(imageFile)
            bitmap = BitmapFactory.decodeStream(fileInputStream)
        } catch (exception : FileNotFoundException) {
            Log.e("getBitmap", exception.toString())
        } finally {
            try {
                fileInputStream?.close()
            } catch (exception : IOException) {
                Log.e("getBitmap", exception.toString())
            }
        }
        return bitmap!!
    }

    /*
    * return byte array from the bitmap
    * quality of the image represents 0 to 100 %
    * */
    fun getBytesFromBitmap(bitmap: Bitmap, quality: Int) : ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun saveImageToFile(imageBitmap: Bitmap) : File? {
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, "${System.currentTimeMillis()}.jpg")
        return try {
            val stream = FileOutputStream(imageFile)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            Toast.makeText(context, "Image Saved successfully", Toast.LENGTH_SHORT).show()
            imageFile
        } catch (e: IOException) {
            Log.e("SaveImage", "Error saving image to file: ${e.message}")
            Toast.makeText(context, "Error saving image to file: ${e.message}", Toast.LENGTH_SHORT).show()
            null
        }
    }
}