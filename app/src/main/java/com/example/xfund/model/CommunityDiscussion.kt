package com.example.xfund.model

import java.util.Date


data class CommunityDiscussion(
    val author: String,
    val title: String,
    val desc: String,
    val tags: List<String>,
    val createdOn: Date
) {
}