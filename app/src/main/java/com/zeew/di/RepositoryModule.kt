package com.zeew.di

import android.content.Context
import com.zeew.network.ZeewApi
import com.zeew.repository.AuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNewsRepository(
        @ApplicationContext appContext: Context,
        zeewApi: ZeewApi
    ): AuthenticationRepository {
        return AuthenticationRepository(zeewApi,appContext)
    }
}