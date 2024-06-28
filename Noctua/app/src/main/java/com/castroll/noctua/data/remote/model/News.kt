package com.castroll.noctua.data.remote.model

data class News (
    val images: List<Images>,
    val link: String,
    val _id: String,
    val hidden: Boolean,
    val tittle: String,
    val body: String,
    val date: String,
    val createdAt: String,
    val updatedAt: String,
    val title: String
)