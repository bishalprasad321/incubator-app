package com.bishal.incubator.models

class User {
    var image: String? = null
    var name: String? = null
    var lowercaseName: String? = null
    var email: String? = null
    var password: String? = null
    var username: String? = null
    var bio: String? = null
    constructor()

    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }
}

