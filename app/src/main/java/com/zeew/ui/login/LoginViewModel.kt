package com.zeew.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.zeew.model.datatransfer.UserResponse
import com.zeew.model.domain.LoginForm
import com.zeew.repository.AuthenticationRepository
import com.zeew.util.RegexValidationUtil
import com.zeew.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authenticationRepository: AuthenticationRepository) :
    ViewModel() {

    private val loginForm = LoginForm()

    private val _loginResource = MutableLiveData<Resource<UserResponse>>()
    val loginResource: LiveData<Resource<UserResponse>>
        get() = _loginResource

    private val _socialLoginResource = MutableLiveData<Resource<String>>()
    val socialLoginResource: LiveData<Resource<String>>
        get() = _socialLoginResource

    val shouldShowEmailValidationError = LiveEvent<Boolean>()

    val shouldShowPasswordValidationError = LiveEvent<Boolean>()

    val navigateToHome = LiveEvent<Boolean>()
    val navigateToSignup = LiveEvent<Boolean>()


    fun onEmailChanged(email: String) {
        loginForm.email = email
    }

    fun onPasswordChanged(password: String) {
        loginForm.password = password
    }

    fun onLoginClick() {
        val isEmailValid = RegexValidationUtil.isEmailValid(loginForm.email)
        val isPasswordValid = RegexValidationUtil.isPasswordValid(loginForm.password)

        if (isEmailValid && isPasswordValid) {
            _loginResource.postValue(Resource.Loading())
            authenticationRepository.login(loginForm)
                .subscribe {
                    if (it.result.success == 1) {
                        authenticationRepository.saveUsername("customerId:${it.result.customerId}")
                        _loginResource.postValue(Resource.Success(it))
                        navigateToHome.postValue(true)
                    } else {
                        _loginResource.postValue(Resource.Error(it.result.message))
                    }
                }
        } else {
            if (!isEmailValid) {
                shouldShowEmailValidationError.postValue(true)
            }
            if (!isPasswordValid) {
                shouldShowPasswordValidationError.postValue(true)
            }
        }
    }

    fun onSignupClick() {
        navigateToSignup.postValue(true)
    }

    fun onUsernameAvailable(username: String?) {
        authenticationRepository.saveUsername(username ?: "")
    }

    fun updateSocialLoginStatus(resource: Resource<String>) {
        _socialLoginResource.postValue(resource)
    }
}