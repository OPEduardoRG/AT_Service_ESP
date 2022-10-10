package com.example.listacomponenteqr.di

import com.example.listacomponenteqr.utils.Constants
import com.example.listacomponenteqr.data.remote.ServiceApi
import com.example.listacomponenteqr.data.repository.ActivateMaquinaRepositoryImpl
import com.example.listacomponenteqr.data.repository.MaquinaRepositoryImpl
import com.example.listacomponenteqr.domain.repository.ActMaquinaRepository
import com.example.listacomponenteqr.domain.repository.MaquinaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMaquinaApi (): ServiceApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServiceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMaquinaRepository(api: ServiceApi): MaquinaRepository{
        return MaquinaRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun ActProvideMaquinaRepository(api: ServiceApi): ActMaquinaRepository{
        return ActivateMaquinaRepositoryImpl(api)
    }
}