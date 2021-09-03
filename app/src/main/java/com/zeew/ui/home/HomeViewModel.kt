package com.zeew.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.zeew.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val authenticationRepository: AuthenticationRepository) :
    ViewModel() {

    private val _userIdentifier = MutableLiveData<String>()
    val userIdentifier: LiveData<String>
        get() = _userIdentifier

    val navigateToLogin = LiveEvent<Boolean>()

    init {
        _userIdentifier.postValue(authenticationRepository.getUsername())
    }

    fun onLogoutClicked() {
        authenticationRepository.deleteUsername()
        navigateToLogin.postValue(true)
    }
}