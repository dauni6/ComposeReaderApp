package com.dontsu.composereaderapp.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dontsu.composereaderapp.data.wrapper.Resource
import com.dontsu.composereaderapp.data.model.Item
import com.dontsu.composereaderapp.data.repository.book.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderBookSearchViewModel @Inject constructor(
    private val repository: BookRepository
): ViewModel() {
    var list: List<Item> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)

    init {
        loadBooks()
    }

    private fun loadBooks() = viewModelScope.launch {
        searchBooks()
    }

    fun searchBooks(query: String = "안드로이드") = viewModelScope.launch {
        if (query.isEmpty()) {
            return@launch
        }

        try {
            when (val response = repository.getBooks(query)) {
                is Resource.Loading -> {
                    isLoading = true
                }
                is Resource.Success -> {
                    list = response.data!!
                    if (list.isNotEmpty()) {
                        isLoading = false
                    }
                }
                is Resource.Error -> {
                    isLoading = false
                    Log.e("TEST", "SearchError : ${response.message}")
                }
            }
        } catch (exception: Exception) {
            isLoading = false
            Log.e("TEST", "SearchError : ${exception.message}")
        }

    }

}
