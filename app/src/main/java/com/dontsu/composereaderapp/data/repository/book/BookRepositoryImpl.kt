package com.dontsu.composereaderapp.data.repository.book

import com.dontsu.composereaderapp.data.wrapper.Resource
import com.dontsu.composereaderapp.data.model.Item
import com.dontsu.composereaderapp.data.network.BooksApi
import com.dontsu.composereaderapp.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception

class BookRepositoryImpl @Inject constructor(
    private val api: BooksApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BookRepository {

    override suspend fun getBooks(searchQuery: String): Resource<List<Item>?> = withContext(ioDispatcher) {
            return@withContext try {
                Resource.Loading(data = true)
                val itemList = api.getAllBooks(searchQuery).items
                Resource.Loading(data = false)
                if (itemList.toString().isNotEmpty()) {
                    Resource.Success(data = itemList)
                } else {
                    Resource.Success(data = emptyList())
                }
            } catch (e: Exception) {
                Resource.Error(message = e.message.toString())
            }
        }

    override suspend fun getBookInfo(bookId: String): Resource<Item> = withContext(ioDispatcher) {
        val response = try {
            Resource.Loading(data = true)
            api.getBookInfo(bookId = bookId)
        } catch (e: Exception) {
            return@withContext Resource.Error(message = "An error occurred ${e.message.toString()}")
        }
        Resource.Loading(data = false)
        Resource.Success(data = response)
    }

}
