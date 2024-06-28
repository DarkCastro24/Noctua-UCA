package com.castroll.noctua.data.remote.model

data class Subject(
    val subject: String,
    val cycle: Int
)

data class User(
    val _id: String,
    val username: String,
    val name: String,
    val type: Int,
    val currentSubjects: List<String>,
    val allSubjects: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val biography: String,
    val career: String,
    val email: String,
    val hobbies: String,
    val phone: String,
    val profilePhoto: String,
    val visible: String?
)

