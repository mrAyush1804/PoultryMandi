package com.ninjafarm.poultrymandi.app.di

import com.ninjafarm.poultrymandi.app.feature.notification.data.repository.NotificationRepositoryImpl
import com.ninjafarm.poultrymandi.app.feature.notification.domian.repository.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository
}