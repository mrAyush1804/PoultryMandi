package com.example.poultrymandi.app.feature.PaperRate.di

import com.example.poultrymandi.app.feature.PaperRate.data.repository.PaperRateRepositoryImpl
import com.example.poultrymandi.app.feature.PaperRate.domain.repository.PaperRateRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PaperRateModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient =
        createSupabaseClient(
            supabaseUrl = "https://qeovnuuipkferrnyuvaz.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFlb3ZudXVpcGtmZXJybnl1dmF6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzUyMjIwMzAsImV4cCI6MjA5MDc5ODAzMH0.cxGp7zrPsPmJ-2_1u5XNeeXGBZ40xoVrd3Md1rBVLuY"
        ) {
            install(Postgrest)
        }

    @Provides
    @Singleton
    fun providePaperRateRepository(
        supabaseClient: SupabaseClient
    ): PaperRateRepository = PaperRateRepositoryImpl(supabaseClient)
}