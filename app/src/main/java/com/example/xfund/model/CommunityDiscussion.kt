package com.example.xfund.model

import java.time.LocalDateTime


data class CommunityDiscussion(
    val author: String,
    val title: String,
    val desc: String,
    val tags: List<String>,
    val createdOn: LocalDateTime
) {
}