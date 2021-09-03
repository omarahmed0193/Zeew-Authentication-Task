package com.zeew.model.domain

data class SignupForm(
    var email: String,
    var password: String,
    var confirmPassword: String,
    var firstName: String,
    var lastName: String,
    var userCountryCode: String = "+20",
    var phoneNumber: String,
    var referralCode: String
)