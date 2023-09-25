package com.example.xfund.model

import com.google.firebase.firestore.auth.User

data class CommunityDiscussion(val id: String, val title: String, val desc: String, val tags: MutableList<String>, val author: User) {

}