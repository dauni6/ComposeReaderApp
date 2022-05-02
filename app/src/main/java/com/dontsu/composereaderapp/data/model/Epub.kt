package com.dontsu.composereaderapp.data.model


import com.google.gson.annotations.SerializedName

data class Epub(
    @SerializedName("acsTokenLink")
    val acsTokenLink: String?,
    @SerializedName("downloadLink")
    val downloadLink: String?,
    @SerializedName("isAvailable")
    val isAvailable: Boolean?
)