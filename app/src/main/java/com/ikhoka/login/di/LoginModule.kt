package com.ikhoka.login.di

import com.ikhoka.login.LoginRepository
import com.ikhoka.login.FakeLoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class CacheReplay

@InstallIn(ViewModelComponent::class)
@Module
object LoginModule {

    @Provides
    @CacheReplay
    fun providesCacheReplay() = 1

    @Provides
    fun provideLoginRepository(): LoginRepository {
        return FakeLoginRepository()
    }
}