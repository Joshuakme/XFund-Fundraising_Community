package com.example.xfund.model

data class PaymentMethod(
    var cardName: String = "" ,
    var cardNo: String = "",
    var cardExpiry: String = "",
    var cardCvv: String = ""
){

}