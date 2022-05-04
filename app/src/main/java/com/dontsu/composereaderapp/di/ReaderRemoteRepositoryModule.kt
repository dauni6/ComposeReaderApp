package com.dontsu.composereaderapp.di

import com.dontsu.composereaderapp.data.repository.BookRepository
import com.dontsu.composereaderapp.data.repository.BookRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ReaderRemoteRepositoryModule {

    @Binds
    abstract fun provideReaderBookRemoteRepository(
        bookRepositoryImpl2: BookRepositoryImpl
    ): BookRepository

}
