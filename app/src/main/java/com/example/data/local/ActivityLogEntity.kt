package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userName: String,
    val userRole: String, // "CS" or "BM"
    val action: String,
    val timestampStr: String,
    val timestampMs: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recipientRole: String, // "CS", "BM", or "ALL"
    val title: String,
    val message: String,
    val timestampStr: String,
    val isRead: Boolean = false,
    val taskId: Long? = null
)
