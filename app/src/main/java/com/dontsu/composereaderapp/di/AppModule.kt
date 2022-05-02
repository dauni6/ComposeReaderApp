package com.dontsu.composereaderapp.di

import com.dontsu.composereaderapp.BuildConfig
import com.dontsu.composereaderapp.data.network.BooksApi
import com.dontsu.composereaderapp.util.Url.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /** Remote */
    @Singleton
    @Provides
    fun providesBooksApi(
        retrofit: Retrofit
    ): BooksApi = retrofit.create(BooksApi::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .readTimeout(timeout = 15, unit = TimeUnit.SECONDS)
            .connectTimeout(timeout = 5, unit =  TimeUnit.SECONDS)
            .addInterceptor(interceptor = interceptor)
            .build()
    }

}
