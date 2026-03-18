package com.example.poultrymandi.app.di

import com.example.poultrymandi.app.feature.home.data.repository.RatesRepositoryImpl
import com.example.poultrymandi.app.feature.home.domain.repository.RatesRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun provideRatesRepository(
        firestore: FirebaseFirestore
    ): RatesRepository {
        return RatesRepositoryImpl(firestore)
    }
}
