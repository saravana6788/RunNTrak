package com.skcodes.auth.domain

class UserDataValidator(private val patternValidator: PatternValidator) {

    fun isValidEmail(email:String):Boolean{
        return patternValidator.matches(email)
    }


    fun isValidPassword(password:String):PasswordValidationState{
        val  hasMinLength = password.length >= MINIMUM_PASSWORD_LENGTH
        val hasNumber = password.any{it.isDigit()}
        val hasLowercase = password.any{ it.isLowerCase()}
        val hasUpperCase  = password.any{it.isUpperCase()}

        return PasswordValidationState(
            hasMinLength = hasMinLength,
            hasNumber = hasNumber,
            hasLowerCaseCharacter = hasLowercase,
            hasUpperCaseCharacter = hasUpperCase
        )

    }

    companion object{
        const val MINIMUM_PASSWORD_LENGTH = 9
    }
}