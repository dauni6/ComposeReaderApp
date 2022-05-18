package com.dontsu.composereaderapp.di

import com.dontsu.composereaderapp.data.repository.book.BookRepository
import com.dontsu.composereaderapp.data.repository.book.BookRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ReaderRemoteRepositoryModule {

    @Binds
    abstract fun provideReaderBookRemoteRepository(
        bookRepositoryImpl: BookRepositoryImpl
    ): BookRepository

}
