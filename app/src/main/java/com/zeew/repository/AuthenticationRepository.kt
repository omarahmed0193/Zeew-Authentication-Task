package com.zeew.repository

import android.content.Context
import com.zeew.R
import com.zeew.model.datatransfer.UserResponse
import com.zeew.model.domain.LoginForm
import com.zeew.model.domain.SignupForm
import com.zeew.network.ZeewApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val zeewApi: ZeewApi,
    private val context: Context
) {

    private val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )

    fun login(loginForm: LoginForm): Observable<UserResponse> =
        zeewApi.login(loginForm.email, loginForm.password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun signup(signupForm: SignupForm): Observable<UserResponse> = zeewApi.signup(
        email = signupForm.email,
        password = signupForm.password,
        firstName = signupForm.firstName,
        lastName = signupForm.lastName,
        userCountryCode = signupForm.userCountryCode,
        phoneNumber = signupForm.phoneNumber,
        referralCode = signupForm.referralCode
    )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun saveUsername(username: String) {
        with(sharedPreferences.edit()) {
            putString(context.getString(R.string.preference_username_key), username)
            apply()
        }
    }

    fun getUsername() =
        sharedPreferences.getString(context.getString(R.string.preference_username_key), "")

    fun deleteUsername() = with(sharedPreferences.edit()) {
        putString(context.getString(R.string.preference_username_key), null)
        apply()
    }

}