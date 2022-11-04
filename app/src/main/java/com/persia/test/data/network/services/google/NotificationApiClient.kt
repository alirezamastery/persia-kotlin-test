package com.persia.test.data.network.services.google

import com.persia.test.data.firebase.PushNotification
import com.persia.test.data.network.CustomResponse
import okhttp3.ResponseBody
import retrofit2.Response


class NotificationApiClient(
    private val notificationService: NotificationService
) {
    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): CustomResponse<T> {
        return try {
            CustomResponse.success(apiCall.invoke())
        } catch (e: Exception) {
            CustomResponse.failure(e)
        }
    }

    suspend fun postNotification(notification: PushNotification): CustomResponse<ResponseBody> {
        return safeApiCall { notificationService.postNotification(notification) }
    }
}