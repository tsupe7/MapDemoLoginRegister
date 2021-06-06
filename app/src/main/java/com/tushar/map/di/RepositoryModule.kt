package com.tushar.map.di

import com.tushar.login.repository.LoginRepository
import com.tushar.login.repository.LoginRepositoryImpl
import com.tushar.map.ui.dashboard.repository.UserRepository
import com.tushar.map.ui.dashboard.repository.UserRepositoryImpl
import com.tushar.registration.repository.RegistrationRepository
import com.tushar.registration.repository.RegistrationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideRegistrationRepository(registrationRepository: RegistrationRepositoryImpl): RegistrationRepository

    @Binds
    fun provideLoginRepository(loginRepository: LoginRepositoryImpl): LoginRepository

    @Binds
    fun provideUserRepository(userRepository: UserRepositoryImpl): UserRepository

}