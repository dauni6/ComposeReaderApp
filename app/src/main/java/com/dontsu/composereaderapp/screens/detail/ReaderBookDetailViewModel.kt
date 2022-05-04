package com.dontsu.composereaderapp.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dontsu.composereaderapp.data.Resource
import com.dontsu.composereaderapp.data.model.Item
import com.dontsu.composereaderapp.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderBookDetailViewModel @Inject constructor(
    private val repository: BookRepository
): ViewModel() {

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        return repository.getBookInfo(bookId = bookId)
    }

}
