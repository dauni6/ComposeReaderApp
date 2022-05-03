package com.dontsu.composereaderapp.di

import com.dontsu.composereaderapp.data.repository.BookRepository2
import com.dontsu.composereaderapp.data.repository.BookRepositoryImpl2
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ReaderRemoteRepositoryModule {

    @Binds
    abstract fun provideReaderBookRemoteRepository(
        bookRepositoryImpl2: BookRepositoryImpl2
    ): BookRepository2

}
