package com.zeew.ui.splash

import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.zeew.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(authenticationRepository: AuthenticationRepository) :
    ViewModel() {

        val navigateToLogin = LiveEvent<Boolean>()
        val navigateToHome = LiveEvent<Boolean>()

    init {
        val username = authenticationRepository.getUsername()
        if (username.isNullOrEmpty()) {
            navigateToLogin.postValue(true)
        } else {
            navigateToHome.postValue(true)
        }

    }
}