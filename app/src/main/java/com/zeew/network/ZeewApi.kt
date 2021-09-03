package com.zeew.network

import com.zeew.model.datatransfer.UserResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ZeewApi {

    companion object {
        const val BASE_URL = "https://store.zeew.eu/v1/"
    }

    @FormUrlEncoded
    @POST("CustomerLogin")
    fun login(
        @Field("username") email: String?,
        @Field("password") password: String?,
        @Field("device_type") deviceType: String? = "ANDROID",
        @Field("device_id") deviceId: String? = "FCM_TOKEN",
    ): Observable<UserResponse>

    @FormUrlEncoded
    @POST("CustomerSignUp")
    fun signup(
        @Field("username") email: String,
        @Field("password") password: String,
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("user_country_code") userCountryCode: String,
        @Field("phone_number") phoneNumber: String,
        @Field("action") action: String? = "CustomerSignUp",
        @Field("referral_code") referralCode: String? = "Nakeeb123"
    ): Observable<UserResponse>
}