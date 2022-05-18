package com.dontsu.composereaderapp.data.repository.firebase

import com.dontsu.composereaderapp.data.model.MBook
import com.dontsu.composereaderapp.data.wrapper.DataOrException
import com.dontsu.composereaderapp.di.IODispatcher
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(): FirebaseRepository {
    private val queryBook: Query = FirebaseFirestore.getInstance().collection("books")

    override suspend fun getAllBooksFromDatabase(): DataOrException<List<MBook>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryBook.get().await().documents.map { snapshot -> // Firebase Query의 await가 suspend function을 지원해준다.
                snapshot.toObject(MBook::class.java)!!
            }

            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false

        } catch (exception: FirebaseFirestoreException) {
            dataOrException.exception = exception
        }

        return dataOrException
    }

}
