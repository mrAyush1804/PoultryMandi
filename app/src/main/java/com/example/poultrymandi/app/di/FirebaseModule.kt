package com.example.poultrymandi.app.di

import android.content.SharedPreferences
import com.example.poultrymandi.app.feature.auth.data.repository.AuthRepositoryImpl
import com.example.poultrymandi.app.feature.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideWebClientId(): String {
        return "1041518266095-j52d0ldnvvholhq0tm5ce0fb5ds73l3u.apps.googleusercontent.com"
    }

/*
    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        sharedPreferences: SharedPreferences,
        firestore: FirebaseFirestore

    ): AuthRepository = AuthRepositoryImpl(auth, sharedPreferences, firestore)

*/


}
