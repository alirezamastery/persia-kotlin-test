package com.persia.test.data.network.services.google

import com.persia.test.data.firebase.PushNotification
import com.persia.test.global.Constants.Companion.FIREBASE_CONTENT_TYPE
import com.persia.test.global.Constants.Companion.FIREBASE_SERVER_kEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationService {

    @Headers("Authorization: key=$FIREBASE_SERVER_kEY", "Content-Type:$FIREBASE_CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(@Body notification: PushNotification): Response<ResponseBody>
}