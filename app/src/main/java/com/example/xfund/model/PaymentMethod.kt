package com.example.xfund.model

import java.net.URL
import java.util.Date

data class PaymentMethod(
    var cardName: String,
    var cardNo: String,
    var cardExpiry: String,
    var cardCvv: String
){

}