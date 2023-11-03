package com.bishal.incubator.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Users (
    @DocumentId
    var id: String? = null,
    @PropertyName("image")
    var image: String? = null,
    @PropertyName("name")
    var name: String? = null,
    @PropertyName("lowercaseName")
    var lowercaseName: String? = null,
    @PropertyName("email")
    var email: String? = null,
    @PropertyName("password")
    var password: String? = null,
    @PropertyName("username")
    var username: String? = null,
    @PropertyName("bio")
    var bio: String? = null
)
