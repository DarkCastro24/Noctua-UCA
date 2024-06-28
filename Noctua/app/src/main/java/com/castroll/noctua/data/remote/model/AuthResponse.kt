package com.castroll.noctua.data.remote.model

data class AuthResponse(
    val user: User?,
    val error: String?
)