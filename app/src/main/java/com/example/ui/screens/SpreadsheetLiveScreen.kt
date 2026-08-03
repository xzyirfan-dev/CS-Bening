package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.theme.BentoBlueHero
import com.example.ui.theme.BentoBlueOnHero
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardDark
import com.example.ui.theme.BentoTextMuted
import com.example.ui.theme.SkyBlue

@Composable
fun SpreadsheetLiveScreen(
    tasks: List<ChecklistTaskEntity>,
    selectedMonth: String,
    selectedDayNum: Int,
    onEditScheduleClick: ((ChecklistTaskEntity) -> Unit)? = null
) {
    val dayTasks = tasks.filter { it.monthYearStr == selectedMonth && it.dayNum == selectedDayNum }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Bento Spreadsheet Metadata Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BentoBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = BentoCardDark)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "LIVE SPREADSHEET SYNC TABLE",
                    color = BentoTextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "PERIODE: $selectedMonth",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pimpinan: Fitria Nor Istiqomah | Cabang: Benings Glow Clinic Gresik",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp
                )
                Text(
                    text = "Jam Kerja: 11:00 - 19:00 | Istirahat: 15:00 - 16:00 | Total Tasks: ${tasks.size}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(BentoBlueHero.copy(alpha = 0.2f))
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "📊 Google Sheets Target: docs.google.com/spreadsheets/d/17TEQF8qRSulzwXPc4Hny19C0VCaaVqYfD1iie5JnfUk",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyBlue
                        )
                        Text(
                            text = "📁 Google Drive Folder: drive.google.com/drive/folders/1C0uiaHQAmtI0pTil1leuEsqYA4vhOTt7",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyBlue
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Bento Container for Data Table
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, BentoBorder, RoundedCornerShape(20.dp))
                .horizontalScroll(scrollState)
        ) {
            Column {
                // Table Header Row
                Row(
                    modifier = Modifier
                        .background(BentoBlueHero)
                        .padding(vertical = 12.dp)
                ) {
                    TableCell("Area / Inventaris", 120.dp, isHeader = true)
                    TableCell("Estimasi", 80.dp, isHeader = true)
                    TableCell("Jadwal Jam", 110.dp, isHeader = true)
                    TableCell("Petugas PIC", 130.dp, isHeader = true)
                    TableCell("Link Foto (Timestamp)", 170.dp, isHeader = true)
                    TableCell("Status Pekerjaan", 110.dp, isHeader = true)
                    TableCell("Approval BM", 100.dp, isHeader = true)
                    TableCell("Grade", 90.dp, isHeader = true)
                    TableCell("Aksi Jadwal", 90.dp, isHeader = true)
                }

                // Table Body Rows
                LazyColumn {
                    items(dayTasks) { task ->
                        Row(
                            modifier = Modifier
                                .border(0.5.dp, BentoBorder)
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TableCell(task.areaName, 120.dp)
                            TableCell("${task.estimateMinutes} Min", 80.dp)
                            TableCell(task.scheduleTime, 110.dp)
                            TableCell(task.picName, 130.dp)
                            TableCell(task.photoUrl ?: task.photoTimestamp ?: "-", 170.dp)

                            Box(modifier = Modifier.width(110.dp), contentAlignment = Alignment.Center) {
                                StatusBadge(status = task.status)
                            }
                            TableCell(task.approvalBm, 100.dp)

                            Box(modifier = Modifier.width(90.dp), contentAlignment = Alignment.Center) {
                                if (task.grade != "Pending") {
                                    GradeBadge(grade = task.grade, score = task.score)
                                } else {
                                    Text("-", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            Box(modifier = Modifier.width(90.dp), contentAlignment = Alignment.Center) {
                                if (onEditScheduleClick != null) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(BentoBlueHero)
                                            .clickable { onEditScheduleClick(task) }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Edit",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BentoBlueOnHero
                                        )
                                    }
                                } else {
                                    Text("-", fontSize = 10.sp, color = BentoTextMuted)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TableCell(text: String, width: androidx.compose.ui.unit.Dp, isHeader: Boolean = false) {
    Box(
        modifier = Modifier
            .width(width)
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            fontSize = if (isHeader) 11.sp else 10.sp,
            fontWeight = if (isHeader) FontWeight.Black else FontWeight.Medium,
            color = if (isHeader) BentoBlueOnHero else MaterialTheme.colorScheme.onSurface
        )
    }
}

