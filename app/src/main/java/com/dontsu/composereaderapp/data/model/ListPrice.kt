package com.dontsu.composereaderapp.data.model


import com.google.gson.annotations.SerializedName

data class ListPrice(
    @SerializedName("amount")
    val amount: Int?,
    @SerializedName("currencyCode")
    val currencyCode: String?
)