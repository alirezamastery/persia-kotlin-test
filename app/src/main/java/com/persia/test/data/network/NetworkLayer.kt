package com.persia.test.data.network

import android.os.Build
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.persia.test.global.Constants
import com.persia.test.data.network.services.persiaatlas.PersiaAtlasService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Duration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkLayer {

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()


    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.SERVER_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(httpClient) // Add our Okhttp client
        .build()


    val persiaAtlasService: PersiaAtlasService by lazy {
        retrofit.create(PersiaAtlasService::class.java)
    }

    val persiaAtlasApi = PersiaAtlasApiClient(persiaAtlasService)


    @Provides
    @Singleton
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val timeoutDuration = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Duration.ofSeconds(15)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        return OkHttpClient.Builder()
            .connectTimeout(timeoutDuration)
            .addInterceptor(AuthInterceptor())
            .writeTimeout(timeoutDuration)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(httpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.SERVER_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesPersiaAtlasService(retrofit: Retrofit): PersiaAtlasService {
        return retrofit.create(PersiaAtlasService::class.java)
    }

    @Provides
    @Singleton
    fun providesPersiaAtlasApi(persiaAtlasService: PersiaAtlasService): PersiaAtlasApiClient {
        return PersiaAtlasApiClient(persiaAtlasService)
    }
}
