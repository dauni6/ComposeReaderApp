package com.dontsu.composereaderapp.screens.detail

import androidx.lifecycle.ViewModel
import com.dontsu.composereaderapp.data.wrapper.Resource
import com.dontsu.composereaderapp.data.model.Item
import com.dontsu.composereaderapp.data.repository.book.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReaderBookDetailViewModel @Inject constructor(
    private val repository: BookRepository
): ViewModel() {

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        return repository.getBookInfo(bookId = bookId)
    }

}
