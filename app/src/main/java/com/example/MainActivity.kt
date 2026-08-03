package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppTopBar
import com.example.ui.components.PeriodFilterDialog
import com.example.ui.components.ScheduleEditModal
import com.example.ui.components.WatermarkPhotoCanvas
import com.example.ui.screens.BmDashboardScreen
import com.example.ui.screens.CsDashboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.NotificationDrawerDialog
import com.example.ui.screens.ReviewTaskDialog
import com.example.ui.screens.SpreadsheetLiveScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = isDarkMode) {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

    val allTasks by viewModel.allTasks.collectAsStateWithLifecycle()
    val recentLogs by viewModel.recentLogs.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    val selectedMonth by viewModel.selectedMonth.collectAsStateWithLifecycle()
    val selectedDayNum by viewModel.selectedDayNum.collectAsStateWithLifecycle()
    val selectedSession by viewModel.selectedSession.collectAsStateWithLifecycle()

    val activePhotoTask by viewModel.activePhotoTask.collectAsStateWithLifecycle()
    val activeReviewTask by viewModel.activeReviewTask.collectAsStateWithLifecycle()
    val activeScheduleTask by viewModel.activeScheduleTask.collectAsStateWithLifecycle()
    val showAddScheduleModal by viewModel.showAddScheduleModal.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Dashboard, 1: Live Spreadsheet, 2: Laporan
    var showNotificationDrawer by remember { mutableStateOf(false) }
    var showPdfFilterDialog by remember { mutableStateOf(false) }

    val unreadNotifs = notifications.count { !it.isRead }

    // If user is not logged in, show LoginScreen directly
    if (currentUser == null) {
        LoginScreen(
            onLoginSubmit = { username, password, callback ->
                viewModel.login(username, password, callback)
            }
        )
        return
    }

    Scaffold(
        topBar = {
            AppTopBar(
                currentUser = currentUser,
                currentRole = currentRole,
                isOnline = isOnline,
                isSyncing = isSyncing,
                isDarkMode = isDarkMode,
                unreadNotificationCount = unreadNotifs,
                onLogout = { viewModel.logout() },
                onRoleChange = { viewModel.setRole(it) },
                onToggleOnline = { viewModel.toggleOnlineStatus() },
                onManualSync = { viewModel.manualSync() },
                onToggleDarkMode = { viewModel.toggleDarkMode() },
                onNotificationClick = { showNotificationDrawer = true }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(imageVector = Icons.Default.Dashboard, contentDescription = null) },
                    label = { Text("Dashboard", fontSize = 11.sp, fontWeight = FontWeight.Black) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(imageVector = Icons.Default.TableChart, contentDescription = null) },
                    label = { Text("Database Sheets", fontSize = 11.sp, fontWeight = FontWeight.Black) }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        showPdfFilterDialog = true
                    },
                    icon = { Icon(imageVector = Icons.Default.Assessment, contentDescription = null) },
                    label = { Text("Laporan PDF", fontSize = 11.sp, fontWeight = FontWeight.Black) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> {
                    if (currentRole == "CS") {
                        CsDashboardScreen(
                            tasks = allTasks,
                            csName = currentUser?.name ?: "Mohammad Rangga adinata",
                            selectedMonth = selectedMonth,
                            selectedDayNum = selectedDayNum,
                            selectedSession = selectedSession,
                            onDaySelected = { viewModel.setSelectedDayNum(it) },
                            onSessionSelected = { viewModel.setSelectedSession(it) },
                            onTaskClick = { viewModel.openPhotoModal(it) }
                        )
                    } else {
                        BmDashboardScreen(
                            tasks = allTasks,
                            activityLogs = recentLogs,
                            onReviewTaskClick = { viewModel.openReviewModal(it) },
                            onEditScheduleClick = { viewModel.openEditScheduleModal(it) },
                            onAddScheduleClick = { viewModel.openAddScheduleModal() },
                            onOpenPdfFilterDialog = { showPdfFilterDialog = true }
                        )
                    }
                }
                1 -> {
                    SpreadsheetLiveScreen(
                        tasks = allTasks,
                        selectedMonth = selectedMonth,
                        selectedDayNum = selectedDayNum,
                        onEditScheduleClick = if (currentRole == "BM") { { viewModel.openEditScheduleModal(it) } } else null
                    )
                }
                2 -> {
                    BmDashboardScreen(
                        tasks = allTasks,
                        activityLogs = recentLogs,
                        onReviewTaskClick = { viewModel.openReviewModal(it) },
                        onEditScheduleClick = { viewModel.openEditScheduleModal(it) },
                        onAddScheduleClick = { viewModel.openAddScheduleModal() },
                        onOpenPdfFilterDialog = { showPdfFilterDialog = true }
                    )
                }
            }

            // Dialogs & Overlays
            // CS Photo Upload Modal
            activePhotoTask?.let { task ->
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { viewModel.closePhotoModal() },
                    title = { Text("FOTO TIMESTAMP & GPS: ${task.areaName}", fontWeight = FontWeight.Bold, fontSize = 15.sp) },
                    text = {
                        WatermarkPhotoCanvas(
                            areaName = task.areaName,
                            scheduleTime = task.scheduleTime,
                            picName = task.picName,
                            existingPhotoUrl = task.photoUrl,
                            existingTimestamp = task.photoTimestamp,
                            existingGps = task.gpsLocation,
                            onPhotoCaptured = { photoPath, ts, gps ->
                                viewModel.submitTaskCompletion(task.id, photoPath, ts, gps)
                            }
                        )
                    },
                    confirmButton = {},
                    dismissButton = {
                        androidx.compose.material3.TextButton(onClick = { viewModel.closePhotoModal() }) {
                            Text("Batal")
                        }
                    }
                )
            }

            // BM Review Modal
            activeReviewTask?.let { task ->
                ReviewTaskDialog(
                    task = task,
                    onDismiss = { viewModel.closeReviewModal() },
                    onSubmitReview = { isApproved, score, grade, catatan ->
                        viewModel.reviewTask(task.id, isApproved, score, grade, catatan)
                    }
                )
            }

            // BM Schedule Edit & Add Modal
            if (activeScheduleTask != null || showAddScheduleModal) {
                ScheduleEditModal(
                    taskToEdit = activeScheduleTask,
                    isAddMode = showAddScheduleModal,
                    selectedDayNum = selectedDayNum,
                    selectedMonth = selectedMonth,
                    onDismiss = { viewModel.closeScheduleModal() },
                    onSaveUpdate = { taskId, areaName, scheduleTime, estimateMinutes, session, picName ->
                        viewModel.updateScheduleTask(taskId, areaName, scheduleTime, estimateMinutes, session, picName)
                    },
                    onSaveAdd = { dayDateStr, dayNum, monthYearStr, session, areaName, estimateMinutes, scheduleTime, picName ->
                        viewModel.addScheduleTask(dayDateStr, dayNum, monthYearStr, session, areaName, estimateMinutes, scheduleTime, picName)
                    }
                )
            }

            // Period Filter Dialog for PDF Export
            if (showPdfFilterDialog) {
                PeriodFilterDialog(
                    onDismiss = { showPdfFilterDialog = false },
                    onGenerateReport = { startDate, endDate ->
                        viewModel.generatePdfReport(startDate, endDate)
                        showPdfFilterDialog = false
                    }
                )
            }

            // Notifications Modal
            if (showNotificationDrawer) {
                NotificationDrawerDialog(
                    notifications = notifications,
                    onDismiss = { showNotificationDrawer = false },
                    onNotificationClick = { viewModel.markNotificationRead(it) }
                )
            }
        }
    }
}

