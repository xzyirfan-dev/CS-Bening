package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ActivityLogEntity
import com.example.data.local.ChecklistTaskEntity
import com.example.ui.components.GradeBadge
import com.example.ui.components.StatusBadge
import com.example.ui.components.TaskCompletionBarChart
import com.example.ui.components.UserRetentionTrendChart
import com.example.ui.theme.AmberPending
import com.example.ui.theme.BentoBlueHero
import com.example.ui.theme.BentoBlueOnHero
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardDark
import com.example.ui.theme.BentoCardVariant
import com.example.ui.theme.BentoMint
import com.example.ui.theme.BentoMintOn
import com.example.ui.theme.BentoTextMuted
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.RoseError
import com.example.ui.theme.SkyBlue

@Composable
fun BmDashboardScreen(
    tasks: List<ChecklistTaskEntity>,
    activityLogs: List<ActivityLogEntity>,
    onReviewTaskClick: (ChecklistTaskEntity) -> Unit,
    onEditScheduleClick: (ChecklistTaskEntity) -> Unit,
    onAddScheduleClick: () -> Unit,
    onOpenPdfFilterDialog: () -> Unit
) {
    val totalMonthTasks = tasks.size
    val completedTasks = tasks.count { it.status == "Selesai" }
    val pendingReviewTasks = tasks.filter { it.status == "Selesai" && it.approvalBm == "Pending" }
    val approvedTasks = tasks.count { it.approvalBm == "Approved" }
    val avgScore = if (tasks.filter { it.score > 0 }.isNotEmpty()) tasks.filter { it.score > 0 }.map { it.score }.average().toInt() else 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Admin Bento Executive Banner Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BentoBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BentoCardDark)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "BENTO MANAGER CONSOLE",
                                color = BentoTextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Aktivitas CS & Audit",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Button(
                            onClick = onOpenPdfFilterDialog,
                            colors = ButtonDefaults.buttonColors(containerColor = BentoMint),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = "PDF",
                                tint = BentoMintOn,
                                modifier = Modifier.width(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Export PDF", fontSize = 11.sp, color = BentoMintOn, fontWeight = FontWeight.ExtraBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Key Bento Metric Cards Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricBentoTile(
                            title = "Total Tugas",
                            value = "$completedTasks / $totalMonthTasks",
                            modifier = Modifier.weight(1f),
                            color = SkyBlue
                        )
                        MetricBentoTile(
                            title = "Butuh Review",
                            value = "${pendingReviewTasks.size}",
                            modifier = Modifier.weight(1f),
                            color = AmberPending
                        )
                        MetricBentoTile(
                            title = "Approved BM",
                            value = "$approvedTasks",
                            modifier = Modifier.weight(1f),
                            color = EmeraldSuccess
                        )
                        MetricBentoTile(
                            title = "Rata Skor",
                            value = "$avgScore",
                            modifier = Modifier.weight(1f),
                            color = Color(0xFFA855F7)
                        )
                    }
                }
            }
        }

        // Section 1: Schedule Management & Assignment for BM
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BentoBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = SkyBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "KELOLA & EDIT JADWAL CS",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 12.sp,
                                    letterSpacing = 0.8.sp,
                                    color = SkyBlue
                                )
                                Text(
                                    text = "BM bertugas mengatur jadwal jam & petugas PIC",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = onAddScheduleClick,
                            colors = ButtonDefaults.buttonColors(containerColor = BentoBlueHero),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("+ Jadwal Baru", fontSize = 11.sp, color = BentoBlueOnHero, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // Section 2: Pending Reviews Queue (For Approval)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.RateReview, contentDescription = null, tint = AmberPending)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "REVIU FOTO CS (${pendingReviewTasks.size})",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        letterSpacing = 0.8.sp,
                        color = BentoTextMuted
                    )
                }
            }
        }

        if (pendingReviewTasks.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BentoBorder, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Semua foto terunggah telah ditinjau & disetujui!",
                            fontSize = 12.sp,
                            color = EmeraldSuccess,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            items(pendingReviewTasks.take(5)) { task ->
                PendingReviewItemCard(task = task, onClick = { onReviewTaskClick(task) })
            }
        }

        // Section 2: Visual Performance & Retention Analytics Charts
        item {
            TaskCompletionBarChart(
                dailyCompletionCounts = listOf(22, 22, 22, 18, 20, 22, 22),
                daysLabels = listOf("Sab 01", "Min 02", "Sen 03", "Sel 04", "Rab 05", "Kam 06", "Jum 07")
            )
        }

        item {
            UserRetentionTrendChart(
                retentionData = listOf(100f, 100f, 100f, 95f, 100f, 100f, 100f)
            )
        }

        // Section 3: Staff Leaderboard & Scorecard Ranking
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BentoBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Leaderboard, contentDescription = null, tint = SkyBlue)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RANKING PETUGAS CS",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            letterSpacing = 0.8.sp,
                            color = BentoTextMuted
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    LeaderboardRow(
                        rank = 1,
                        name = "Fitria Nor Istiqomah",
                        completedCount = completedTasks,
                        excellentCount = tasks.count { it.grade == "Excellent" },
                        score = avgScore
                    )
                }
            }
        }

        // Section 4: Real-time User Activity Monitoring Feed
        item {
            Text(
                text = "LOG AKTIVITAS REAL-TIME",
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                letterSpacing = 0.8.sp,
                color = BentoTextMuted
            )
        }

        items(activityLogs.take(8)) { log ->
            ActivityLogRow(log = log)
        }
    }
}

@Composable
private fun MetricBentoTile(title: String, value: String, modifier: Modifier, color: Color) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(BentoCardVariant)
            .border(1.dp, BentoBorder, RoundedCornerShape(16.dp))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title.uppercase(), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = BentoTextMuted)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Black, color = color)
        }
    }
}

@Composable
private fun PendingReviewItemCard(task: ChecklistTaskEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BentoBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${task.areaName} (${task.dayDateStr})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = "Jadwal: ${task.scheduleTime} | PIC: ${task.picName}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Foto & Timestamp: ${task.photoTimestamp ?: "Selesai"}",
                    fontSize = 10.sp,
                    color = SkyBlue,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = AmberPending),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Review", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
private fun LeaderboardRow(rank: Int, name: String, completedCount: Int, excellentCount: Int, score: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, BentoBorder, RoundedCornerShape(16.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(BentoBlueHero)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(text = "#$rank", color = BentoBlueOnHero, fontWeight = FontWeight.Black, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = "Selesai: $completedCount | Excellent: $excellentCount", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        GradeBadge(grade = "Excellent", score = score)
    }
}

@Composable
private fun ActivityLogRow(log: ActivityLogEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BentoBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (log.userRole == "CS") BentoBlueHero else BentoMint)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = log.userRole,
                    color = if (log.userRole == "CS") BentoBlueOnHero else BentoMintOn,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${log.userName}: ${log.action}", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                Text(text = log.timestampStr, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

