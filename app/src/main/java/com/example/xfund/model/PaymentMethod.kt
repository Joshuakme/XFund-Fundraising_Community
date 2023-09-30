package com.example.xfund.model

import com.google.firebase.firestore.DocumentId

data class PaymentMethod(
    @DocumentId val id: String? = null,
    var cardName: String = "",
    var cardNo: String = "",
    var cardExpiry: String = "",
    var cardCvv: String = ""
){

}