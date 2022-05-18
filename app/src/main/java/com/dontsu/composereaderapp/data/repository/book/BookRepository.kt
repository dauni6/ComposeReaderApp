package com.dontsu.composereaderapp.data.repository.book

import com.dontsu.composereaderapp.data.wrapper.Resource
import com.dontsu.composereaderapp.data.model.Item

interface BookRepository {
    suspend fun getBooks(searchQuery: String): Resource<List<Item>?>
    suspend fun getBookInfo(bookId: String): Resource<Item>
}
