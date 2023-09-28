package com.example.xfund.model

import java.util.Date

data class Project(
    val name: String,
    val projectStartDate: Date,
    val projectEndDate: Date,
    val fundingTarget: Int,
    var collectedFund: Int,
    val description: String,
) {
}