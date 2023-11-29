package com.bishal.incubator.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class ImageManager {

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
}