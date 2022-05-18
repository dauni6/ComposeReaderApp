package com.dontsu.composereaderapp.data.repository.firebase

import com.dontsu.composereaderapp.data.model.MBook
import com.dontsu.composereaderapp.data.wrapper.DataOrException

interface FirebaseRepository {

    suspend fun getAllBooksFromDatabase(): DataOrException<List<MBook>, Boolean, Exception>

}
