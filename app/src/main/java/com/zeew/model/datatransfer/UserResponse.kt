package com.zeew.model.datatransfer


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "result")
    val result: Result,
    @Json(name = "status")
    val status: String
) {
    @JsonClass(generateAdapter = true)
    data class Result(
        @Json(name = "customerId")
        val customerId: Int?,
        @Json(name = "message")
        val message: String,
        @Json(name = "success")
        val success: Int,
        @Json(name = "token")
        val token: String?
    )
}