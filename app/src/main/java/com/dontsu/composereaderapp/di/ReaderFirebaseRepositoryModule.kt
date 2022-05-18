package com.dontsu.composereaderapp.di

import com.dontsu.composereaderapp.data.repository.firebase.FirebaseRepository
import com.dontsu.composereaderapp.data.repository.firebase.FirebaseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ReaderFirebaseRepositoryModule {

    @Binds
    abstract fun provideReaderFirebaseRepository(
        firebaseRepositoryImpl: FirebaseRepositoryImpl
    ): FirebaseRepository


}