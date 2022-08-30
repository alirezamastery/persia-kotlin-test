package com.persia.test.data.network

import com.persia.test.global.AppPreferences
import com.persia.test.data.network.services.persiaatlas.responses.RefreshToken
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.lang.Exception


class AuthInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        // If token has been saved, add it to the request
        val accessToken = AppPreferences.accessToken
        Timber.i("access token in interceptor: $accessToken")
        accessToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        var response = chain.proceed(requestBuilder.build())
        Timber.i("response code: ${response.code}")
        if (response.code == 403) {
            response.close()
            Timber.i("Authentication failed - response code: ${response.code}")
            val refreshToken = AppPreferences.refreshToken
            if (refreshToken != null) {
                // val retrofitInstance = RetrofitInstance()
                AppPreferences.testToken = false
                runBlocking {
                    val payload = RefreshToken(refresh = refreshToken)
                    Timber.i("using refresh token: ${refreshToken}")
                    val refreshResponse = NetworkLayer.persiaAtlasService.refreshToken(payload)

                    if (refreshResponse.isSuccessful) {
                        Timber.i("refreshing token success: ${refreshResponse.body()}")
                        val data = refreshResponse.body()!!
                        AppPreferences.accessToken = data.access
                        AppPreferences.refreshToken = data.refresh
                        requestBuilder.removeHeader("Authorization")
                        requestBuilder.addHeader(
                            "Authorization",
                            "Bearer ${AppPreferences.accessToken}"
                        )
                        response = chain.proceed(requestBuilder.build())
                        Timber.i("response after token refreshing - code: ${response.code}")
                    } else {
                        Timber.i("refreshing token failed code: ${refreshResponse.code()}")
                        throw Exception("refresh token failed")
                        // TODO: navigate to Login screen
                    }
                }
            } else {
                Timber.i("no refresh token found")
                // TODO: navigate to Login screen
            }
        }

        return response
    }

    private fun useRefreshToken() {
        val refreshToken = AppPreferences.refreshToken
        if (refreshToken != null) {
            // val retrofitInstance = RetrofitInstance()
            runBlocking {
                val payload = RefreshToken(refresh = refreshToken)
                val response = NetworkLayer.persiaAtlasService.refreshToken(payload)
                if (response.isSuccessful) {
                    val data = response.body()!!
                    AppPreferences.accessToken = data.access
                    AppPreferences.refreshToken = data.refresh
                }
            }
        } else {
            // TODO: navigate to Login screen
        }
    }
}

// Another way of getting retrofit instance that was not so nice:
// /**
//  * Session manager to save and fetch data from SharedPreferences
//  */
// class SessionManager(context: Context) {
//
//     private var prefs: SharedPreferences = context.getSharedPreferences(
//         context.getString(R.string.app_name), Context.MODE_PRIVATE
//     )
//
//     companion object {
//
//         const val USER_ACCESS_TOKEN = "user_access_token"
//         const val USER_REFRESH_TOKEN = "user_refresh_token"
//     }
//
//
//     fun saveAuthToken(accessToken: String) {
//         val editor = prefs.edit()
//         editor.putString(USER_ACCESS_TOKEN, accessToken)
//         editor.apply()
//     }
//
//     fun saveRefreshToken(refreshToken: String) {
//         val editor = prefs.edit()
//         editor.putString(USER_REFRESH_TOKEN, refreshToken)
//         editor.apply()
//     }
//
//     fun fetchAuthToken(): String? {
//         return prefs.getString(USER_ACCESS_TOKEN, null)
//     }
//
//     fun fetchRefreshToken(): String? {
//         return prefs.getString(USER_REFRESH_TOKEN, null)
//     }
// }

// class AuthInterceptor(context: Context) : Interceptor {
//
//     private val sessionManager = SessionManager(context)
//
//     override fun intercept(chain: Interceptor.Chain): Response {
//         val requestBuilder = chain.request().newBuilder()
//
//         // If token has been saved, add it to the request
//         sessionManager.fetchAuthToken()?.let {
//             requestBuilder.addHeader("Authorization", "Bearer $it")
//         }
//
//         return chain.proceed(requestBuilder.build())
//     }
// }

// class RetrofitInstance {
//
//     private val retrofit = Retrofit.Builder()
//         .baseUrl(BASE_URL)
//         .addConverterFactory(MoshiConverterFactory.create(moshi))
//         .addCallAdapterFactory(CoroutineCallAdapterFactory())
//         .client(okhttpClient()) // Add our Okhttp client
//         .build()
//
//     private lateinit var apiService: PersiaAtlasApi
//
//     fun getApiService(): PersiaAtlasApi {
//
//         // Initialize ApiService if not initialized yet
//         if (!::apiService.isInitialized) {
//             val retrofit = Retrofit.Builder()
//                 .baseUrl(BASE_URL)
//                 .addConverterFactory(GsonConverterFactory.create())
//                 .client(okhttpClient(context)) // Add our Okhttp client
//                 .build()
//
//
//             apiService = retrofit.create(PersiaAtlasApi::class.java)
//         }
//
//         return apiService
//     }
//
//     /**
//      * Initialize OkhttpClient with our interceptor
//      */
//     private fun okhttpClient(): OkHttpClient {
//         return OkHttpClient.Builder()
//             .addInterceptor(AuthInterceptor())
//             .build()
//     }
// }

// retrofitInstance.getApiService(context).login(payload)
//     .enqueue(object : Callback<LoginResponse> {
//         override fun onResponse(
//             call: Call<LoginResponse>,
//             response: Response<LoginResponse>
//         ) {
//             Timber.i("response came: ${response.body()} | code: ${response.code()}")
//             _eventLoginSuccess.value = true
//             if (response.code() == 200 && response.body() != null) {
//                 // val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
//                 val data = response.body()!!
//                 Timber.i("response body: ${data}")
//                 // val editor = sharedPref.edit()
//                 // editor.putString("userMobile", mobile)
//                 // editor.putBoolean("isLoggedIn", true)
//                 // editor.putString("accessToken", data.access)
//                 // editor.putString("refreshToken", data.refresh)
//                 // editor.apply()
//                 // sessionManager.saveAuthToken(data.access)
//                 // sessionManager.saveRefreshToken(data.refresh)
//             }
//         }
//
//         override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//             _eventLoginSuccess.value = false
//             Timber.i("error in login request")
//         }
//     })