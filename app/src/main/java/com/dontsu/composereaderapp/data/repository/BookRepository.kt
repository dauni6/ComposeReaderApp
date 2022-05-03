package com.dontsu.composereaderapp.data.repository

import com.dontsu.composereaderapp.data.DataOrException
import com.dontsu.composereaderapp.data.model.Item
import kotlin.Exception

/** 사용 안 함 */
interface BookRepository {
    suspend fun getBooks(searchQuery: String): DataOrException<List<Item>, Boolean, Exception>
    suspend fun getBookInfo(bookId: String): DataOrException<Item, Boolean, Exception>
}
