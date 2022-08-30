package com.persia.test.data.network

import retrofit2.Response

data class CustomResponse<T>(
    val status: Status,
    val data: Response<T>?,
    val exception: Exception?
) {

    companion object {

        fun <T> success(data: Response<T>): CustomResponse<T> {
            return CustomResponse(
                status = Status.Success,
                data = data,
                exception = null
            )
        }

        fun <T> failure(exception: Exception): CustomResponse<T> {
            return CustomResponse(
                status = Status.Failure,
                data = null,
                exception = exception
            )
        }
    }

    sealed class Status {
        object Success : Status()
        object Failure : Status()
    }

    val failed: Boolean
        get() = this.status == Status.Failure

    val isSuccessful: Boolean
        get() = !failed && this.data?.isSuccessful == true

    val body: T
        get() = this.data!!.body()!!

    val bodyNullable: T?
        get() = this.data?.body()
}