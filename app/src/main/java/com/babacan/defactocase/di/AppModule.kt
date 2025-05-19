package com.babacan.defactocase.di

import android.content.Context
import androidx.room.Room
import com.babacan.defactocase.common.DefaultStringProvider
import com.babacan.defactocase.common.StringProvider
import com.babacan.defactocase.data.ApiConstants
import com.babacan.defactocase.data.PreferenceManager
import com.babacan.defactocase.data.room.DeFactoDatabase
import com.babacan.defactocase.data.service.MoviesApiService
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideStringProvider(@ApplicationContext context: Context): StringProvider {
        return DefaultStringProvider(context)
    }

    @Singleton
    @Provides
    internal fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
    }

    @Singleton
    @Provides
    internal fun provideRetrofit(retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.build()
    }

    @Singleton
    @Provides
    internal fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    internal fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: ApiKeyInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyInterceptor).build()

    }

    @Provides
    @Singleton
    fun provideSearchService(retrofitBuilder: Retrofit.Builder): MoviesApiService {
        return retrofitBuilder
            .build()
            .create(MoviesApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDeFactoDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        DeFactoDatabase::class.java,
        "deFacto_database"
    ).build()

    @Singleton
    @Provides
    fun provideDeFactoDao(database: DeFactoDatabase) = database.deFactoDao()

    @Singleton
    @Provides
    fun provideThemePreferenceManager(@ApplicationContext context: Context): PreferenceManager {
        return PreferenceManager(context)
    }
}