package com.persia.test.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.persia.test.Constants
import com.persia.test.data.network.services.persiaatlas.PersiaAtlasService


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

    val persiaAtlasApi = ApiClient(persiaAtlasService)
}
