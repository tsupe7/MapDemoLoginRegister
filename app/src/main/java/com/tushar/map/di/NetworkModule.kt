package com.tushar.map.di

import android.content.Context
import com.tushar.map.BuildConfig
import com.tushar.map.ui.dashboard.service.UserService
import com.tushar.map.ui.login.service.LoginService
import com.tushar.map.ui.registration.service.RegistrationService
import com.tushar.network.Networking
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun getLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }

    @Provides
    @Singleton
    fun provideNetwork(
        @ApplicationContext appContext: Context
    ): Retrofit =
        Networking.create(
            BuildConfig.BASE_URL,
            appContext.cacheDir,
            10 * 1024 * 1024, // 10MB
            getLoggingInterceptor(),
        )

    @Provides
    @Singleton
    fun provideRegistrationService(retrofit: Retrofit): RegistrationService =
        retrofit.create(RegistrationService::class.java)

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)


}