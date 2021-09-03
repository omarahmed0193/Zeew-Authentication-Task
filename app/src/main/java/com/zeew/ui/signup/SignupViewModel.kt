package com.zeew.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.zeew.model.datatransfer.UserResponse
import com.zeew.model.domain.SignupForm
import com.zeew.repository.AuthenticationRepository
import com.zeew.util.RegexValidationUtil
import com.zeew.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val authenticationRepository: AuthenticationRepository) :
    ViewModel() {


    val shouldShowEmailValidationError = LiveEvent<Boolean>()
    val shouldShowPasswordValidationError = LiveEvent<Boolean>()
    val shouldShowConfirmPasswordValidationError = LiveEvent<Boolean>()
    val shouldShowFirstNameValidationError = LiveEvent<Boolean>()
    val shouldShowLastNameValidationError = LiveEvent<Boolean>()
    val shouldShowPhoneNumberValidationError = LiveEvent<Boolean>()

    val navigateToHome = LiveEvent<Boolean>()

    private val signupForm = SignupForm("", "", "", "", "", "", "", "")

    private val _signupResource = MutableLiveData<Resource<UserResponse>>()
    val signupResource: LiveData<Resource<UserResponse>>
        get() = _signupResource

    fun onSignupClick() {
        val isEmailValid = RegexValidationUtil.isEmailValid(signupForm.email)
        val isPasswordValid = RegexValidationUtil.isPasswordValid(signupForm.password)
        val isConfirmPasswordValid =
            RegexValidationUtil.isPasswordValid(signupForm.confirmPassword) && signupForm.password == signupForm.confirmPassword
        val isFirstNameValid = RegexValidationUtil.isNameValid(signupForm.firstName)
        val isLastNameValid = RegexValidationUtil.isNameValid(signupForm.lastName)
        val isPhoneNumberValid = RegexValidationUtil.isPhoneNumberValid(signupForm.phoneNumber)

        if (isEmailValid && isPasswordValid && isConfirmPasswordValid
            && isFirstNameValid && isLastNameValid && isPhoneNumberValid
        ) {
            _signupResource.postValue(Resource.Loading())
            authenticationRepository.signup(signupForm)
                .subscribe {
                    if (it.result.success == 1) {
                        authenticationRepository.saveUsername("customerId:${it.result.customerId}")
                        _signupResource.postValue(Resource.Success(it))
                        navigateToHome.postValue(true)
                    } else {
                        _signupResource.postValue(Resource.Error(it.result.message))
                    }
                }
        } else {
            if (!isEmailValid) {
                shouldShowEmailValidationError.postValue(true)
            }
            if (!isPasswordValid) {
                shouldShowPasswordValidationError.postValue(true)
            }
            if (!isConfirmPasswordValid) {
                shouldShowConfirmPasswordValidationError.postValue(true)
            }
            if (!isFirstNameValid) {
                shouldShowFirstNameValidationError.postValue(true)
            }
            if (!isLastNameValid) {
                shouldShowLastNameValidationError.postValue(true)
            }
            if (!isPhoneNumberValid) {
                shouldShowPhoneNumberValidationError.postValue(true)
            }
        }
    }

    fun onEmailChanged(email: String) {
        signupForm.email = email
    }

    fun onPasswordChanged(password: String) {
        signupForm.password = password
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        signupForm.confirmPassword = confirmPassword
    }

    fun onFirstNameChanged(firstName: String) {
        signupForm.firstName = firstName
    }

    fun onLastNameChanged(lastName: String) {
        signupForm.lastName = lastName
    }

    fun onReferralCodeChanged(referralCode: String) {
        signupForm.referralCode = referralCode
    }

    fun onPhoneNumberChanged(phoneNumber: String) {
        signupForm.phoneNumber = phoneNumber
    }
}