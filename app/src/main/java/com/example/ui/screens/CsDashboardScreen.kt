package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
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
import com.example.data.local.ChecklistTaskEntity
import com.example.ui.components.GradeBadge
import com.example.ui.components.StatusBadge
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
import com.example.ui.theme.RoseError
import com.example.ui.theme.SkyBlue

@Composable
fun CsDashboardScreen(
    tasks: List<ChecklistTaskEntity>,
    csName: String = "Mohammad Rangga adinata",
    selectedMonth: String,
    selectedDayNum: Int,
    selectedSession: String,
    onDaySelected: (Int) -> Unit,
    onSessionSelected: (String) -> Unit,
    onTaskClick: (ChecklistTaskEntity) -> Unit
) {
    val dayFilteredTasks = tasks.filter {
        it.monthYearStr == selectedMonth && it.dayNum == selectedDayNum
    }

    val sessionFilteredTasks = if (selectedSession == "ALL") {
        dayFilteredTasks
    } else {
        dayFilteredTasks.filter { it.session.startsWith(selectedSession) }
    }

    val totalDayTasks = dayFilteredTasks.size
    val completedDayTasks = dayFilteredTasks.count { it.status == "Selesai" }
    val pendingDayTasks = dayFilteredTasks.count { it.status == "Pending" }
    val revisionDayTasks = dayFilteredTasks.count { it.status == "Revisi" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Bento Hero Scorecard Card (Ice Blue Accent Tile)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BentoBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BentoBlueHero)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "BENTO SCORECARD CS",
                            color = BentoBlueOnHero.copy(alpha = 0.7f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(BentoBlueOnHero.copy(alpha = 0.12f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "AGUSTUS 2026",
                                color = BentoBlueOnHero,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = csName,
                        color = BentoBlueOnHero,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Shift: 11:00 - 19:00 (Istirahat 15:00 - 16:00)",
                        color = BentoBlueOnHero.copy(alpha = 0.75f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ScoreMetricBentoTile(
                            title = "Total Tugas",
                            value = "$completedDayTasks / $totalDayTasks",
                            modifier = Modifier.weight(1f)
                        )
                        ScoreMetricBentoTile(
                            title = "Pending",
                            value = "$pendingDayTasks",
                            modifier = Modifier.weight(1f)
                        )
                        ScoreMetricBentoTile(
                            title = "Revisi",
                            value = "$revisionDayTasks",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Date Selector Bento Grid Scroll
        item {
            Column {
                Text(
                    text = "TANGGAL JADWAL (BENTO SELECTOR)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.8.sp,
                    color = BentoTextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items((1..31).toList()) { day ->
                        val isSelected = day == selectedDayNum
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isSelected) BentoBlueHero else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) BentoBlueHero else BentoBorder,
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable { onDaySelected(day) }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "TGL",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) BentoBlueOnHero.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$day",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isSelected) BentoBlueOnHero else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Session Selector Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SessionChip("SEMUA SESI", selectedSession == "ALL") { onSessionSelected("ALL") }
                SessionChip("SESI 1 (11:00-15:00)", selectedSession == "SESI 1") { onSessionSelected("SESI 1") }
                SessionChip("SESI 2 (16:00-19:00)", selectedSession == "SESI 2") { onSessionSelected("SESI 2") }
            }
        }

        // Tasks List Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AREAL & CHECKLIST TUGAS",
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    letterSpacing = 0.8.sp,
                    color = BentoTextMuted
                )
                Text(
                    text = "${sessionFilteredTasks.size} Area",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Tasks List Items
        if (sessionFilteredTasks.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BentoBorder, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Tidak ada tugas pada filter ini.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(sessionFilteredTasks) { task ->
                TaskItemCard(task = task, onClick = { onTaskClick(task) })
            }
        }
    }
}

@Composable
private fun ScoreMetricBentoTile(title: String, value: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(BentoBlueOnHero.copy(alpha = 0.08f))
            .border(1.dp, BentoBlueOnHero.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title.uppercase(),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = BentoBlueOnHero.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = BentoBlueOnHero
            )
        }
    }
}

@Composable
private fun SessionChip(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) BentoMint else MaterialTheme.colorScheme.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) BentoMint else BentoBorder
        )
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (isSelected) BentoMintOn else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TaskItemCard(
    task: ChecklistTaskEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BentoBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(BentoBlueHero.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = task.areaName.take(3).uppercase(),
                            fontWeight = FontWeight.Black,
                            color = SkyBlue,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = task.areaName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Jadwal: ${task.scheduleTime} (${task.estimateMinutes} Mins)",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                StatusBadge(status = task.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Catatan BM if Revisi
            if (task.status == "Revisi" && task.catatanBm.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(RoseError.copy(alpha = 0.12f))
                        .border(1.dp, RoseError.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = RoseError,
                            modifier = Modifier.width(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Catatan BM: ${task.catatanBm}",
                            fontSize = 11.sp,
                            color = RoseError,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Photo Watermark Info or Capture Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (task.status == "Selesai" || task.photoUrl != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = EmeraldSuccess,
                            modifier = Modifier.width(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Timestamp & GPS Verifikatif",
                            fontSize = 11.sp,
                            color = EmeraldSuccess,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = null,
                            tint = SkyBlue,
                            modifier = Modifier.width(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Klik Foto Watermark GPS",
                            fontSize = 11.sp,
                            color = SkyBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (task.grade != "Pending") {
                    GradeBadge(grade = task.grade, score = task.score)
                }
            }
        }
    }
}

