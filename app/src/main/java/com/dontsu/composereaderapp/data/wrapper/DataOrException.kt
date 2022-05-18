package com.dontsu.composereaderapp.data.wrapper

/** 사용 안 함 */
data class DataOrException<T, Boolean, E : Exception>(
    var data: T? = null,
    var loading: Boolean? = null,
    var exception: E? = null
)
