package com.bishal.incubator.models

class User {
    var image: String? = null
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var username: String? = null
    var bio: String? = null
    constructor()
    constructor(image: String?, name: String?, email: String?, password: String?, username: String?, bio: String?) {
        this.image = image
        this.name = name
        this.email = email
        this.password = password
        this.username = username
        this.bio = bio
    }

    constructor(name: String?, email: String?, password: String?) {
        this.name = name
        this.email = email
        this.password = password
    }

    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }


}
