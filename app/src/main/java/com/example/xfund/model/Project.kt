package com.example.xfund.model

import java.util.Date

data class Project(
    val cover: Int,
    val name: String,
    val start_date: Date,
    val end_date: Date,
    val fund_target: Int,
    var fund_collected: Int,
    val description: String
) {
    val percentage: Int
        get() = ((fund_collected.toFloat() / fund_target) * 100).toInt()

    val percentageValue: String = percentage.toString()


}