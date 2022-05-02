package com.dontsu.composereaderapp.data.model


import com.google.gson.annotations.SerializedName

data class RetailPrice(
    @SerializedName("amountInMicros")
    val amountInMicros: Long?,
    @SerializedName("currencyCode")
    val currencyCode: String?
)