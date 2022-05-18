package com.dontsu.composereaderapp.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dontsu.composereaderapp.data.model.MBook
import com.dontsu.composereaderapp.data.repository.firebase.FirebaseRepository
import com.dontsu.composereaderapp.data.wrapper.DataOrException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderHomeScreenViewModel @Inject constructor(
    private val repository: FirebaseRepository
) : ViewModel() {

    val data: MutableState<DataOrException<List<MBook>, Boolean, Exception>> = mutableStateOf(
        DataOrException(listOf(), true, Exception(""))
    )

    init {
        getAllBooksFromDatabase()
    }

    private fun getAllBooksFromDatabase() = viewModelScope.launch {
        data.value.loading = true
        data.value = repository.getAllBooksFromDatabase()
        if (!data.value.data.isNullOrEmpty()) {
            Log.d("TEST", "getAllBooksFromDatabase() : ${data.value.data}")
            data.value.loading = false
        }
    }

}
