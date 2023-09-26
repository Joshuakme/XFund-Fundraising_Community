package com.example.xfund.model

import java.net.URL
import java.util.Date

data class PaymentMethod(
    val cardName: String,
    val cardNo: String,
    val ExpiryDate: Date,
    val CVV: String,
    val cardType: URL
) {
}