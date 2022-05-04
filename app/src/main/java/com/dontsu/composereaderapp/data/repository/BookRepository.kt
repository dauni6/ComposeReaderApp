package com.dontsu.composereaderapp.data.repository

import com.dontsu.composereaderapp.data.Resource
import com.dontsu.composereaderapp.data.model.Item

interface BookRepository {
    suspend fun getBooks(searchQuery: String): Resource<List<Item>?>
    suspend fun getBookInfo(bookId: String): Resource<Item>
}
