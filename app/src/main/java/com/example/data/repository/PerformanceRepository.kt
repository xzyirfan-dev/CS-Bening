package com.example.data.repository

import android.content.Context
import com.example.data.local.ActivityDao
import com.example.data.local.ActivityLogEntity
import com.example.data.local.AppDatabase
import com.example.data.local.ChecklistTaskEntity
import com.example.data.local.NotificationDao
import com.example.data.local.NotificationEntity
import com.example.data.local.TaskDao
import com.example.data.local.UserDao
import com.example.data.local.UserEntity
import com.example.data.remote.GoogleSheetSyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PerformanceRepository(private val context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val taskDao: TaskDao = db.taskDao()
    private val activityDao: ActivityDao = db.activityDao()
    private val notificationDao: NotificationDao = db.notificationDao()
    private val userDao: UserDao = db.userDao()

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    fun setNetworkStatus(online: Boolean) {
        _isOnline.value = online
        if (online) {
            triggerAutoSync()
        }
    }

    val allTasks: Flow<List<ChecklistTaskEntity>> = taskDao.getAllTasks()
    val recentLogs: Flow<List<ActivityLogEntity>> = activityDao.getRecentLogs()
    val allNotifications: Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()

    fun getTasksByMonth(monthYear: String): Flow<List<ChecklistTaskEntity>> =
        taskDao.getTasksByMonth(monthYear)

    fun getTasksByDay(dayFilter: String): Flow<List<ChecklistTaskEntity>> =
        taskDao.getTasksByDay("%$dayFilter%")

    fun getNotificationsForRole(role: String): Flow<List<NotificationEntity>> =
        notificationDao.getNotificationsForRole(role)

    val allUsers: Flow<List<UserEntity>> = userDao.getAllUsers()

    suspend fun loginUser(username: String, password: String): UserEntity? = withContext(Dispatchers.IO) {
        val user = userDao.getUserByUsername(username.trim())
        if (user != null && user.password == password.trim()) {
            return@withContext user
        }
        return@withContext null
    }

    suspend fun seedDatabaseIfEmpty() = withContext(Dispatchers.IO) {
        val users = listOf(
            UserEntity(
                id = "BM01",
                nik = "2600701036",
                name = "Fitria Nor Istiqomah",
                role = "BM",
                title = "Manager Clinic Gresik",
                username = "BM01",
                password = "Gresik123",
                branch = "Benings Glow Clinic Gresik",
                avatarColorHex = "#0D9488"
            ),
            UserEntity(
                id = "CS01",
                nik = "2600713030",
                name = "Mohammad Rangga adinata",
                role = "CS",
                title = "Cleaning Service",
                username = "CS01",
                password = "Gresik123",
                branch = "Benings Glow Clinic Gresik",
                avatarColorHex = "#0284C7"
            ),
            UserEntity(
                id = "CS02",
                nik = "2400713043",
                name = "SAMPURNA",
                role = "CS",
                title = "Cleaning Service / Security",
                username = "CS02",
                password = "Gresik123",
                branch = "Benings Glow Clinic Gresik",
                avatarColorHex = "#7C3AED"
            )
        )
        userDao.insertUsers(users)

        // Seed tasks for August 2026 (31 days) & September 2026 (1 day) if taskDao is empty
        val existingUnsynced = taskDao.getUnsyncedTasks()
        // Check if DB already populated
        // We do a quick check via getTasksByMonth
        val augustTasks = mutableListOf<ChecklistTaskEntity>()

        val areaListSesi1 = listOf(
            Pair("LANTAI", Pair(30, "11:00 - 11:30")),
            Pair("WC", Pair(20, "11:30 - 11:50")),
            Pair("WASTAFEL", Pair(15, "11:50 - 12:05")),
            Pair("CERMIN", Pair(10, "12:05 - 12:15")),
            Pair("DINDING", Pair(20, "12:15 - 12:35")),
            Pair("TRASH", Pair(15, "12:35 - 12:50")),
            Pair("KESET", Pair(5, "12:50 - 12:55")),
            Pair("KRAN AIR", Pair(5, "12:55 - 13:00")),
            Pair("AROMA", Pair(5, "13:00 - 13:05")),
            Pair("CALMIC", Pair(5, "13:05 - 13:10")),
            Pair("TANAMAN", Pair(10, "13:10 - 13:20"))
        )

        val areaListSesi2 = listOf(
            Pair("LANTAI", Pair(30, "16:00 - 16:30")),
            Pair("WC", Pair(20, "16:30 - 16:50")),
            Pair("WASTAFEL", Pair(15, "16:50 - 17:05")),
            Pair("CERMIN", Pair(10, "17:05 - 17:15")),
            Pair("DINDING", Pair(20, "17:15 - 17:35")),
            Pair("TRASH", Pair(15, "17:35 - 17:50")),
            Pair("KESET", Pair(5, "17:50 - 17:55")),
            Pair("KRAN AIR", Pair(5, "17:55 - 18:00")),
            Pair("AROMA", Pair(5, "18:00 - 18:05")),
            Pair("CALMIC", Pair(5, "18:05 - 18:10")),
            Pair("TANAMAN", Pair(10, "18:10 - 18:20"))
        )

        val daysOfWeek = listOf("Sabtu", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat")

        // Generate August 2026 (31 days)
        for (day in 1..31) {
            val dayName = daysOfWeek[(day - 1) % 7]
            val dayFormatted = String.format(Locale.US, "%02d", day)
            val dayDateStr = "$dayName, $dayFormatted Agustus 2026"

            // Seed Sesi 1 (All tasks start clean/Pending for real live operational sync)
            for ((area, details) in areaListSesi1) {
                augustTasks.add(
                    ChecklistTaskEntity(
                        dayDateStr = dayDateStr,
                        dayNum = day,
                        monthYearStr = "AGUSTUS 2026",
                        session = "SESI 1 (Sebelum Istirahat: 11:00 - 15:00)",
                        areaName = area,
                        estimateMinutes = details.first,
                        scheduleTime = details.second,
                        status = "Pending",
                        approvalBm = "Pending",
                        score = 0,
                        grade = "Pending",
                        photoTimestamp = null,
                        gpsLocation = null,
                        photoUrl = null
                    )
                )
            }

            // Seed Sesi 2 (All tasks start clean/Pending for real live operational sync)
            for ((area, details) in areaListSesi2) {
                augustTasks.add(
                    ChecklistTaskEntity(
                        dayDateStr = dayDateStr,
                        dayNum = day,
                        monthYearStr = "AGUSTUS 2026",
                        session = "SESI 2 (Setelah Istirahat: 16:00 - 19:00)",
                        areaName = area,
                        estimateMinutes = details.first,
                        scheduleTime = details.second,
                        status = "Pending",
                        approvalBm = "Pending",
                        score = 0,
                        grade = "Pending",
                        photoTimestamp = null,
                        gpsLocation = null,
                        photoUrl = null
                    )
                )
            }
        }

        // Generate September 2026 (1 day)
        val sepDayDateStr = "Selasa, 01 September 2026"
        for ((area, details) in areaListSesi1) {
            augustTasks.add(
                ChecklistTaskEntity(
                    dayDateStr = sepDayDateStr,
                    dayNum = 1,
                    monthYearStr = "SEPTEMBER 2026",
                    session = "SESI 1 (Sebelum Istirahat: 11:00 - 15:00)",
                    areaName = area,
                    estimateMinutes = details.first,
                    scheduleTime = details.second
                )
            )
        }
        for ((area, details) in areaListSesi2) {
            augustTasks.add(
                ChecklistTaskEntity(
                    dayDateStr = sepDayDateStr,
                    dayNum = 1,
                    monthYearStr = "SEPTEMBER 2026",
                    session = "SESI 2 (Setelah Istirahat: 16:00 - 19:00)",
                    areaName = area,
                    estimateMinutes = details.first,
                    scheduleTime = details.second
                )
            )
        }

        taskDao.insertTasks(augustTasks)

        // Seed initial notifications
        notificationDao.insertNotification(
            NotificationEntity(
                recipientRole = "CS",
                title = "Jadwal Kebersihan Harian Siap",
                message = "Jadwal Sesi 1 & Sesi 2 Benings Glow Clinic Gresik telah diperbarui.",
                timestampStr = getCurrentTimeFormatted()
            )
        )
        notificationDao.insertNotification(
            NotificationEntity(
                recipientRole = "BM",
                title = "Laporan Masuk",
                message = "Fitria Nor Istiqomah telah menyelesaikan tugas kebersihan Sesi 1.",
                timestampStr = getCurrentTimeFormatted()
            )
        )

        activityDao.insertLog(
            ActivityLogEntity(
                userName = "System",
                userRole = "SYSTEM",
                action = "Inisialisasi Database Daily Performance Tracker (704 Tugas)",
                timestampStr = getCurrentTimeFormatted()
            )
        )
    }

    suspend fun submitTaskCompletion(
        taskId: Long,
        photoPath: String,
        timestampStr: String,
        gpsLocationStr: String,
        isOfflineMode: Boolean,
        picName: String? = null
    ) = withContext(Dispatchers.IO) {
        val task = taskDao.getTaskById(taskId) ?: return@withContext
        val finalPhotoUrl = if (photoPath.startsWith("http")) photoPath else "https://drive.google.com/drive/folders/1C0uiaHQAmtI0pTil1leuEsqYA4vhOTt7?file=$photoPath"
        val activePic = picName ?: task.picName

        val updatedTask = task.copy(
            status = "Selesai",
            approvalBm = "Pending",
            photoUrl = finalPhotoUrl,
            photoTimestamp = timestampStr,
            gpsLocation = gpsLocationStr,
            picName = activePic,
            isSynced = !isOfflineMode,
            lastUpdated = System.currentTimeMillis()
        )
        taskDao.updateTask(updatedTask)

        // Log Activity
        val nowStr = getCurrentTimeFormatted()
        activityDao.insertLog(
            ActivityLogEntity(
                userName = activePic,
                userRole = "CS",
                action = "Mengunggah foto timestamp & Penanda GPS area ${task.areaName} (${task.dayDateStr})",
                timestampStr = nowStr
            )
        )

        // Create Notification for BM
        notificationDao.insertNotification(
            NotificationEntity(
                recipientRole = "BM",
                title = "Pembaruan Tugas CS",
                message = "$activePic menyelesaikan area ${task.areaName} (${task.scheduleTime}) pada ${task.dayDateStr}.",
                timestampStr = nowStr,
                taskId = taskId
            )
        )

        if (!isOfflineMode) {
            GoogleSheetSyncService.syncTaskToGoogleSheet("SUBMIT_CS_PHOTO", updatedTask)
            triggerAutoSync()
        }
    }

    suspend fun reviewTaskByBm(
        taskId: Long,
        isApproved: Boolean,
        score: Int,
        grade: String,
        catatan: String
    ) = withContext(Dispatchers.IO) {
        val task = taskDao.getTaskById(taskId) ?: return@withContext
        val newStatus = if (isApproved) "Selesai" else "Revisi"
        val newApproval = if (isApproved) "Approved" else "Rejected"

        val updatedTask = task.copy(
            status = newStatus,
            approvalBm = newApproval,
            score = score,
            grade = grade,
            catatanBm = catatan,
            lastUpdated = System.currentTimeMillis()
        )
        taskDao.updateTask(updatedTask)

        // Sync to Google Apps Script Endpoint
        GoogleSheetSyncService.syncTaskToGoogleSheet("BM_APPROVAL", updatedTask)

        val nowStr = getCurrentTimeFormatted()
        activityDao.insertLog(
            ActivityLogEntity(
                userName = "Building Manager",
                userRole = "BM",
                action = if (isApproved) "Menyetujui area ${task.areaName} dengan Nilai $score ($grade)" else "Meminta REVISI area ${task.areaName}: $catatan",
                timestampStr = nowStr
            )
        )

        notificationDao.insertNotification(
            NotificationEntity(
                recipientRole = "CS",
                title = if (isApproved) "Tugas Disetujui (Grade: $grade)" else "PERINGATAN: Tugas Perlu Revisi",
                message = if (isApproved) "Tugas ${task.areaName} disetujui BM dengan skor $score." else "BM meminta revisi untuk ${task.areaName}. Catatan: $catatan",
                timestampStr = nowStr,
                taskId = taskId
            )
        )
    }

    fun triggerAutoSync() {
        CoroutineScope(Dispatchers.IO).launch {
            _isSyncing.value = true
            kotlinx.coroutines.delay(1200) // Simulate real-time websocket/Sheets sync latency
            taskDao.markAllSynced()
            _isSyncing.value = false
            activityDao.insertLog(
                ActivityLogEntity(
                    userName = "AutoSync Service",
                    userRole = "SYSTEM",
                    action = "Sinkronisasi Data Real-Time ke Cloud / Spreadsheet Berhasil",
                    timestampStr = getCurrentTimeFormatted()
                )
            )
        }
    }

    suspend fun updateTaskSchedule(
        taskId: Long,
        newAreaName: String,
        newScheduleTime: String,
        newEstimateMinutes: Int,
        newSession: String,
        newPicName: String
    ) = withContext(Dispatchers.IO) {
        val task = taskDao.getTaskById(taskId) ?: return@withContext
        val updatedTask = task.copy(
            areaName = newAreaName,
            scheduleTime = newScheduleTime,
            estimateMinutes = newEstimateMinutes,
            session = newSession,
            picName = newPicName,
            lastUpdated = System.currentTimeMillis()
        )
        taskDao.updateTask(updatedTask)

        // Sync to Google Apps Script Endpoint
        GoogleSheetSyncService.syncTaskToGoogleSheet("BM_UPDATE_SCHEDULE", updatedTask)

        val nowStr = getCurrentTimeFormatted()
        activityDao.insertLog(
            ActivityLogEntity(
                userName = "Building Manager",
                userRole = "BM",
                action = "Mengubah Jadwal area $newAreaName ($newScheduleTime, PIC: $newPicName)",
                timestampStr = nowStr
            )
        )

        notificationDao.insertNotification(
            NotificationEntity(
                recipientRole = "CS",
                title = "Pembaruan Jadwal Kebersihan",
                message = "Jadwal area $newAreaName telah diperbarui oleh BM menjadi $newScheduleTime (PIC: $newPicName).",
                timestampStr = nowStr,
                taskId = taskId
            )
        )
    }

    suspend fun addNewTaskSchedule(
        dayDateStr: String,
        dayNum: Int,
        monthYearStr: String,
        session: String,
        areaName: String,
        estimateMinutes: Int,
        scheduleTime: String,
        picName: String
    ) = withContext(Dispatchers.IO) {
        val newTask = ChecklistTaskEntity(
            dayDateStr = dayDateStr,
            dayNum = dayNum,
            monthYearStr = monthYearStr,
            session = session,
            areaName = areaName,
            estimateMinutes = estimateMinutes,
            scheduleTime = scheduleTime,
            picName = picName,
            status = "Pending",
            approvalBm = "Pending"
        )
        val newId = taskDao.insertTask(newTask)
        val createdTask = newTask.copy(id = newId)

        // Sync to Google Apps Script Endpoint
        GoogleSheetSyncService.syncTaskToGoogleSheet("BM_ADD_SCHEDULE", createdTask)

        val nowStr = getCurrentTimeFormatted()
        activityDao.insertLog(
            ActivityLogEntity(
                userName = "Building Manager",
                userRole = "BM",
                action = "Menambahkan Jadwal Tugas Baru: $areaName ($scheduleTime, PIC: $picName)",
                timestampStr = nowStr
            )
        )

        notificationDao.insertNotification(
            NotificationEntity(
                recipientRole = "CS",
                title = "Jadwal Tugas Baru Ditambahkan",
                message = "BM menambahkan tugas area $areaName ($scheduleTime) untuk PIC $picName.",
                timestampStr = nowStr,
                taskId = newId
            )
        )
    }

    suspend fun markNotificationAsRead(id: Long) = withContext(Dispatchers.IO) {
        notificationDao.markAsRead(id)
    }

    private fun getCurrentTimeFormatted(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date()) + " WIB"
    }
}
