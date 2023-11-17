package com.bishal.incubator.utils

import java.io.File

class FileSearch {

    /*
    * Search a directory and return a list of **directories** contained inside
    * @param directory
    * @return
    * */
    fun getDirectoryPaths(directory: String) : ArrayList<String> {
        val pathArray : ArrayList<String> = arrayListOf()
        val file = File(directory)
        val listFiles = file.listFiles()
        if (listFiles != null) {
            (listFiles.indices).forEach { i ->
                if (listFiles[i].isDirectory) {
                    pathArray.add(listFiles[i].absolutePath)
                }
            }
        }
        return pathArray
    }

    /*
    * Search a directory and return a list of **files** contained inside
    * @param directory
    * @return
    * */
    fun getFilePaths(directory: String) : ArrayList<String> {
        val pathArray : ArrayList<String> = arrayListOf()
        val file = File(directory)
        val listFiles = file.listFiles()
        if (listFiles != null) {
            (listFiles.indices).forEach { i ->
                if (listFiles[i].isFile) {
                    pathArray.add(listFiles[i].absolutePath)
                }
            }
        }
        return pathArray
    }

}