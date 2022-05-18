package com.dontsu.composereaderapp.data.model


import com.google.firebase.auth.FirebaseAuth
import com.google.gson.annotations.SerializedName

data class VolumeInfo(
    @SerializedName("allowAnonLogging")
    val allowAnonLogging: Boolean?,
    @SerializedName("authors")
    val authors: List<String>?,
    @SerializedName("canonicalVolumeLink")
    val canonicalVolumeLink: String?,
    @SerializedName("categories")
    val categories: List<String>?,
    @SerializedName("contentVersion")
    val contentVersion: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("imageLinks")
    val imageLinks: ImageLinks?,
    @SerializedName("industryIdentifiers")
    val industryIdentifiers: List<IndustryIdentifier>?,
    @SerializedName("infoLink")
    val infoLink: String?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("maturityRating")
    val maturityRating: String?,
    @SerializedName("pageCount")
    val pageCount: Int?,
    @SerializedName("panelizationSummary")
    val panelizationSummary: PanelizationSummary?,
    @SerializedName("previewLink")
    val previewLink: String?,
    @SerializedName("printType")
    val printType: String?,
    @SerializedName("publishedDate")
    val publishedDate: String?,
    @SerializedName("publisher")
    val publisher: String?,
    @SerializedName("readingModes")
    val readingModes: ReadingModes?,
    @SerializedName("subtitle")
    val subtitle: String?,
    @SerializedName("title")
    val title: String?
) {
    fun toMBook(googleBookId: String?): MBook {
        return MBook(
            userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
            title = this.title,
            authors = this.authors.toString(),
            description = this.description,
            categories = categories.toString(),
            notes = "",
            photoUrl = this.imageLinks?.thumbnail.toString(),
            publishedDate = this.publishedDate,
            pageCount = this.pageCount.toString(),
            rating = 0.0,
            googleBookId = googleBookId
        )
    }
}