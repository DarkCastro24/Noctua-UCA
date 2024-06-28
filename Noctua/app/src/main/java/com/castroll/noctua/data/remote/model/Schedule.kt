package com.castroll.noctua.data.remote.model

data class Schedule(
    val date: String,
    val hour: String,
    val activity: String?,
    val available: Boolean = true
)