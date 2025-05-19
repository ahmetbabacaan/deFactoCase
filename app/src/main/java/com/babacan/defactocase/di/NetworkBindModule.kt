package com.babacan.defactocase.di

import com.babacan.defactocase.data.repository.MoviesRepositoryImpl
import com.babacan.defactocase.domain.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class NetworkBindModule {
    @Binds
    abstract fun bindSearchRepository(searchRepositoryImpl: MoviesRepositoryImpl): MoviesRepository
}