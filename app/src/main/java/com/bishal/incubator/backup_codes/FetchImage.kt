package com.bishal.incubator.backup_codes

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.bishal.incubator.models.ImageItem

class FetchImageActivity : AppCompatActivity() {
    fun fetchImages(context: Context): ArrayList<ImageItem> {
        /*val images = mutableListOf<ImageItem>()
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                val imagePath = it.getString(columnIndex)
                images.add(ImageItem(imagePath))
            }
        }

        return images*/

        val galleryImages = ArrayList<ImageItem>()
        val columns = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID
        ) //get all columns of type images

        val orderBy = MediaStore.Images.Media.DATE_TAKEN //order data by date


        val imageCursor: Cursor = managedQuery(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
            null, "$orderBy DESC"
        ) //get all data in Cursor by sorting in DESC order


        for (i in 0 until imageCursor.count) {
            imageCursor.moveToPosition(i)
            val dataColumnIndex =
                imageCursor.getColumnIndex(MediaStore.Images.Media.DATA) //get column index
            galleryImages.add(ImageItem(imageCursor.getString(dataColumnIndex))) //get Image from column index
        }
        return galleryImages
    }
}