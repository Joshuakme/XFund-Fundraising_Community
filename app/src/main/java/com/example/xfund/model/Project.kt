package com.example.xfund.model

import com.example.xfund.News
import com.example.xfund.User
import java.util.Date

data class Project(
    val projectName: String,
    val projectStartDate: Date,
    val projectEndDate: Date,
    val fundingTarget: Int,
    var collectedFund: Int,
    val NumOfSupporter: Int,
    val about: String,
    val backerList: MutableList<User>,
    val newsList: MutableList<News>
) {
}