package com.persia.test.modules


// @Module
// @InstallIn(SingletonComponent::class)
// object NetworkRefreshTokenModule {
//
//     @Provides
//     @Singleton
//     @Named("persiaRefreshTokenMoshi")
//     fun providePersiaRefreshTokenMoshi(): Moshi {
//         return Moshi.Builder()
//             .add(KotlinJsonAdapterFactory())
//             .build()
//     }
//
//     @Provides
//     @Singleton
//     @Named("persiaRefreshTokenOkHttpClient")
//     fun provideOkHttpClient(): OkHttpClient {
//         val timeoutDuration = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//             Duration.ofSeconds(15)
//         } else {
//             TODO("VERSION.SDK_INT < O")
//         }
//         return OkHttpClient.Builder()
//             .connectTimeout(timeoutDuration)
//             .addInterceptor(HttpLoggingInterceptor().apply {
//                 setLevel(HttpLoggingInterceptor.Level.BODY)
//             })
//             .writeTimeout(timeoutDuration)
//             .build()
//     }
//
//     @Provides
//     @Singleton
//     @Named("persiaRefreshTokenRetrofit")
//     fun provideRetrofit(
//         @Named("persiaRefreshTokenOkHttpClient") httpClient: OkHttpClient,
//         @Named("persiaRefreshTokenMoshi") moshi: Moshi
//     ): Retrofit {
//         return Retrofit.Builder()
//             .baseUrl(Constants.SERVER_BASE_URL)
//             .addConverterFactory(MoshiConverterFactory.create(moshi))
//             .addCallAdapterFactory(CoroutineCallAdapterFactory())
//             .client(httpClient)
//             .build()
//     }
//
//     @Provides
//     @Singleton
//     @Named("persiaRefreshTokenService")
//     fun providePersiaRefreshTokenService(
//         @Named("persiaRefreshTokenRetrofit") retrofit: Retrofit
//     ): PersiaAtlasService {
//         return retrofit.create(PersiaAtlasService::class.java)
//     }
//
//     @Provides
//     @Singleton
//     @Named("persiaRefreshTokenApiClient")
//     fun providePersiaAtlasApi(
//         @Named("persiaRefreshTokenService") persiaAtlasService: PersiaAtlasService
//     ): ApiClient {
//         return ApiClient(persiaAtlasService)
//     }
// }