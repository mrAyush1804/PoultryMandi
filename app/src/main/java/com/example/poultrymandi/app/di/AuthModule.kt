package com.example.poultrymandi.app.di

import com.example.poultrymandi.app.feature.auth.domain.repository.AuthRepository
import com.example.poultrymandi.app.feature.auth.data.repository.AuthRepositoryImpl // Adjust path
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    
}