package com.castroll.noctua.data.remote.model

data class Lab(
    val _id: String,
    val labnumber: String,
    val description: String,
    val alumAmount: Int,
    val urlImage: String,
    val schedule: List<Schedule>,
    val createdAt: String,
    val updatedAt: String
)

