package com.bishal.incubator.models

data class Photo(
    var imagePath: String? = null,
    var dateCreated: String? = null,
    var caption : String? = null,
    var postId : String? = null,
    var tags : String? = null
)
