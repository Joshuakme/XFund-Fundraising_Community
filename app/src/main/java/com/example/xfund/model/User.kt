package com.example.xfund.model

import android.net.Uri

data class User (
    val uid : String = "",
    val displayName : String? = "User",
    val email : String? = "",
    val imgUri : Uri? = null)