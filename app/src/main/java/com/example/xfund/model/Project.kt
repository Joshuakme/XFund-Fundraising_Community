package com.example.xfund.model

import com.example.xfund.News
import com.example.xfund.User
import java.util.Date

data class Project(
    val name: String,
    val star_date: Date,
    val end_date: Date,
    val fund_target: Int,
    var fund_collected: Int,
    val description: String/*,
    val backer_list: MutableList<User>,
    val news_list: MutableList<News>*/
) {



}