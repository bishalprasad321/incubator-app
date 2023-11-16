package com.bishal.incubator.utils

import android.os.Environment

class FilePaths {

    val ROOT_DIR : String = Environment.getExternalStorageDirectory().path
    val PICTURES : String = "${ROOT_DIR}/Pictures"
    val CAMERA : String = "${ROOT_DIR}/DCIM/camera"
}