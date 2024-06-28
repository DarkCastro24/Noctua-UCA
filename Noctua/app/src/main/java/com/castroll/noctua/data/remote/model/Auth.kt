package com.castroll.noctua.data.remote.model

data class LoginRequest(val username: String, val password: String)

data class RegisterRequest(val username: String, val name: String, val password: String, val profilePhoto: String, val type: Int)

