package com.zeew.util

import java.util.regex.Pattern

class RegexValidationUtil {

    companion object {

        fun isNameValid(username: String): Boolean {
            val regexMatcher = Pattern.compile("^.{3,}")
            return regexMatcher.matcher(username).matches()
        }

        fun isPasswordValid(password: String): Boolean {
            val regexMatcher = Pattern.compile("^.{6,}")
            return regexMatcher.matcher(password).matches()
        }

        fun isPhoneNumberValid(phoneNumber: String): Boolean {
            val regexMatcher = Pattern.compile("^[1-9][0-9]{9}")
            return regexMatcher.matcher(phoneNumber).matches()
        }

        fun isEmailValid(email: String): Boolean {
            val regexMatcher = Pattern.compile("^([\\w.\\-]+)@([\\w\\-]+)((\\.(\\w){2,})+)")
            return regexMatcher.matcher(email).matches()
        }
    }

}