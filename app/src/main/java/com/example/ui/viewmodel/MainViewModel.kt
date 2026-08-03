package com.example.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ActivityLogEntity
import com.example.data.local.ChecklistTaskEntity
import com.example.data.local.NotificationEntity
import com.example.data.local.UserEntity
import com.example.data.repository.PdfReportExporter
import com.example.data.repository.PerformanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PerformanceRepository(application)

    // Current Authenticated User state
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.seedDatabaseIfEmpty()
        }
    }

    fun login(username: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val user = repository.loginUser(username, password)
            if (user != null) {
                _currentUser.value = user
                _currentRole.value = user.role
                onResult(true, null)
            } else {
                onResult(false, "Username atau Password salah!")
            }
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    // Role state: "CS" (Cleaning Service) or "BM" (Building Manager)
    private val _currentRole = MutableStateFlow("CS")
    val currentRole: StateFlow<String> = _currentRole.asStateFlow()

    fun setRole(role: String) {
        val userRole = _currentUser.value?.role
        if (userRole == "CS" && role == "BM") {
            _currentRole.value = "CS"
            return
        }
        _currentRole.value = role
    }

    // Dark Mode state
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    // Network / Sync Status
    val isOnline: StateFlow<Boolean> = repository.isOnline
    val isSyncing: StateFlow<Boolean> = repository.isSyncing

    fun toggleOnlineStatus() {
        repository.setNetworkStatus(!isOnline.value)
    }

    fun manualSync() {
        repository.triggerAutoSync()
    }

    // Filter states
    private val _selectedMonth = MutableStateFlow("AGUSTUS 2026")
    val selectedMonth: StateFlow<String> = _selectedMonth.asStateFlow()

    private val _selectedDayNum = MutableStateFlow(1)
    val selectedDayNum: StateFlow<Int> = _selectedDayNum.asStateFlow()

    private val _selectedSession = MutableStateFlow("ALL") // "ALL", "SESI 1", "SESI 2"
    val selectedSession: StateFlow<String> = _selectedSession.asStateFlow()

    fun setSelectedMonth(month: String) {
        _selectedMonth.value = month
    }

    fun setSelectedDayNum(dayNum: Int) {
        _selectedDayNum.value = dayNum
    }

    fun setSelectedSession(session: String) {
        _selectedSession.value = session
    }

    // Tasks list flow
    val allTasks: StateFlow<List<ChecklistTaskEntity>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentLogs: StateFlow<List<ActivityLogEntity>> = repository.recentLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Task for Photo Upload (CS Modal)
    private val _activePhotoTask = MutableStateFlow<ChecklistTaskEntity?>(null)
    val activePhotoTask: StateFlow<ChecklistTaskEntity?> = _activePhotoTask.asStateFlow()

    fun openPhotoModal(task: ChecklistTaskEntity) {
        _activePhotoTask.value = task
    }

    fun closePhotoModal() {
        _activePhotoTask.value = null
    }

    fun submitTaskCompletion(taskId: Long, photoPath: String, tsStr: String, gpsStr: String) {
        viewModelScope.launch {
            val csName = currentUser.value?.name ?: "Mohammad Rangga adinata"
            repository.submitTaskCompletion(taskId, photoPath, tsStr, gpsStr, !isOnline.value, csName)
            _activePhotoTask.value = null
        }
    }

    // Active Task for Review (BM Modal)
    private val _activeReviewTask = MutableStateFlow<ChecklistTaskEntity?>(null)
    val activeReviewTask: StateFlow<ChecklistTaskEntity?> = _activeReviewTask.asStateFlow()

    fun openReviewModal(task: ChecklistTaskEntity) {
        _activeReviewTask.value = task
    }

    fun closeReviewModal() {
        _activeReviewTask.value = null
    }

    fun reviewTask(taskId: Long, isApproved: Boolean, score: Int, grade: String, catatan: String) {
        viewModelScope.launch {
            repository.reviewTaskByBm(taskId, isApproved, score, grade, catatan)
            _activeReviewTask.value = null
        }
    }

    // PDF Export State
    private val _pdfFile = MutableStateFlow<File?>(null)
    val pdfFile: StateFlow<File?> = _pdfFile.asStateFlow()

    fun generatePdfReport(startDateStr: String, endDateStr: String) {
        viewModelScope.launch {
            val tasksToExport = allTasks.value.filter {
                it.monthYearStr == selectedMonth.value
            }
            val file = PdfReportExporter.generatePdfReport(
                context = getApplication(),
                startDateStr = startDateStr,
                endDateStr = endDateStr,
                monthPeriodStr = selectedMonth.value,
                tasks = tasksToExport
            )
            _pdfFile.value = file
            Toast.makeText(getApplication(), "Laporan PDF Berhasil Di-export!", Toast.LENGTH_SHORT).show()
        }
    }

    fun markNotificationRead(id: Long) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    // Schedule Edit & Add Modal State (BM)
    private val _activeScheduleTask = MutableStateFlow<ChecklistTaskEntity?>(null)
    val activeScheduleTask: StateFlow<ChecklistTaskEntity?> = _activeScheduleTask.asStateFlow()

    private val _showAddScheduleModal = MutableStateFlow(false)
    val showAddScheduleModal: StateFlow<Boolean> = _showAddScheduleModal.asStateFlow()

    fun openEditScheduleModal(task: ChecklistTaskEntity) {
        _activeScheduleTask.value = task
    }

    fun openAddScheduleModal() {
        _showAddScheduleModal.value = true
    }

    fun closeScheduleModal() {
        _activeScheduleTask.value = null
        _showAddScheduleModal.value = false
    }

    fun updateScheduleTask(
        taskId: Long,
        newAreaName: String,
        newScheduleTime: String,
        newEstimateMinutes: Int,
        newSession: String,
        newPicName: String
    ) {
        viewModelScope.launch {
            repository.updateTaskSchedule(
                taskId, newAreaName, newScheduleTime, newEstimateMinutes, newSession, newPicName
            )
            closeScheduleModal()
            Toast.makeText(getApplication(), "Jadwal berhasil diperbarui oleh BM!", Toast.LENGTH_SHORT).show()
        }
    }

    fun addScheduleTask(
        dayDateStr: String,
        dayNum: Int,
        monthYearStr: String,
        session: String,
        areaName: String,
        estimateMinutes: Int,
        scheduleTime: String,
        picName: String
    ) {
        viewModelScope.launch {
            repository.addNewTaskSchedule(
                dayDateStr, dayNum, monthYearStr, session, areaName, estimateMinutes, scheduleTime, picName
            )
            closeScheduleModal()
            Toast.makeText(getApplication(), "Jadwal baru berhasil dibuat!", Toast.LENGTH_SHORT).show()
        }
    }
}
