package com.example.bloggers.domain.data

import retrofit2.Response

sealed class ApiResponse<T> {
    companion object {
        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                // Empty body
                if (body == null || response.code() == 204) {
                    ApiSuccessEmptyResponse()
                } else {
                    ApiSuccessResponse(body)
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMessage = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(errorMessage ?: "Unknown error")
            }
        }
    }
}

class ApiSuccessResponse<T>(val data: T) : ApiResponse<T>()
class ApiSuccessEmptyResponse<T> : ApiResponse<T>()
class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()

sealed class Result<out T> {
    data class Success<T>(val data: T?) : Result<T>()
    data class Failure(val statusCode: Int?) : Result<Nothing>()
    object NetworkError : Result<Nothing>()
}

