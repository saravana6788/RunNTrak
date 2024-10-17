package com.skcodes.auth.domain

interface PatternValidator {

    fun matches(email:String):Boolean
}