package com.dontsu.composereaderapp.data.repository

import com.dontsu.composereaderapp.data.DataOrException
import com.dontsu.composereaderapp.data.model.Item
import com.dontsu.composereaderapp.data.network.BooksApi
import com.dontsu.composereaderapp.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception

/** 사용 안 함 */
class BookRepositoryImpl @Inject constructor(
    private val api: BooksApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): BookRepository {
    private val dataOrException = DataOrException<List<Item>, Boolean, Exception>()

    private val bookInfoDataOrException = DataOrException<Item, Boolean, Exception>()

    override suspend fun getBooks(searchQuery: String): DataOrException<List<Item>, Boolean, Exception> = withContext(ioDispatcher) {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllBooks(searchQuery).items
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false

        }catch (e: Exception) {
            dataOrException.exception = e
        }
        dataOrException
    }

    override suspend fun getBookInfo(bookId: String): DataOrException<Item, Boolean, Exception> = withContext(ioDispatcher) {
        try {
            bookInfoDataOrException.loading = true
            bookInfoDataOrException.data = api.getBookInfo(bookId = bookId)
            if (bookInfoDataOrException.data.toString().isNotEmpty()) bookInfoDataOrException.loading = false
        } catch (e: Exception) {
            bookInfoDataOrException.exception = e
        }
        bookInfoDataOrException
    }

}
