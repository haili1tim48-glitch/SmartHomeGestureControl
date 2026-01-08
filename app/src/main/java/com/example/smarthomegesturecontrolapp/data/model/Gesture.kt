package com.example.smarthomegesturecontrolapp.data.model

data class Gesture(
    val id: String,
    val name: String,
    val downloadUrl: String,
    val localPath: String? = null // This will hold the file path once downloaded
)