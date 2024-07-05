package com.ilabs.comeunity.Models

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val residence: String = "",
    val flatNumber: String = "",
    val role: String = "Resident"
)
