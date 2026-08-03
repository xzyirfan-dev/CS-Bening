package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String, // "BM01", "CS01", "CS02"
    val nik: String = "",
    val name: String,
    val role: String, // "CS" or "BM"
    val title: String, // "Manager Clinic Gresik", "Cleaning Service", etc.
    val username: String = "", // "BM01", "CS01", "CS02"
    val password: String = "Gresik123",
    val branch: String = "Benings Glow Clinic Gresik",
    val avatarColorHex: String = "#0284C7"
)
