package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_tasks")
data class ChecklistTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dayDateStr: String, // e.g., "Sabtu, 01 Agustus 2026"
    val dayNum: Int, // e.g. 1
    val monthYearStr: String, // e.g., "AGUSTUS 2026"
    val session: String, // "SESI 1 (Sebelum Istirahat: 11:00 - 15:00)" or "SESI 2 (Setelah Istirahat: 16:00 - 19:00)"
    val areaName: String, // LANTAI, WC, WASTAFEL, CERMIN, DINDING, TRASH, KESET, KRAN AIR, AROMA, CALMIC, TANAMAN
    val estimateMinutes: Int, // e.g., 30, 20, 15
    val scheduleTime: String, // e.g., "11:00 - 11:30"
    val photoUrl: String? = null, // Path or link to photo
    val photoTimestamp: String? = null, // Watermark timestamp string
    val gpsLocation: String? = null, // GPS latitude, longitude & address
    val status: String = "Pending", // "Pending", "Selesai", "Revisi"
    val approvalBm: String = "Pending", // "Pending", "Approved", "Rejected"
    val score: Int = 0, // 0 to 100
    val grade: String = "Pending", // "Excellent", "Good", "Normal", "Poor", "Pending"
    val catatanBm: String = "",
    val picName: String = "Mohammad Rangga adinata",
    val isSynced: Boolean = true,
    val lastUpdated: Long = System.currentTimeMillis()
)
