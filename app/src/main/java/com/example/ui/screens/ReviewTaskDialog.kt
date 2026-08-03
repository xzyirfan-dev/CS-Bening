package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ChecklistTaskEntity
import com.example.ui.components.GradeBadge
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.RoseError
import com.example.ui.theme.SkyBlue

@Composable
fun ReviewTaskDialog(
    task: ChecklistTaskEntity,
    onDismiss: () -> Unit,
    onSubmitReview: (isApproved: Boolean, score: Int, grade: String, catatan: String) -> Unit
) {
    var scoreValue by remember { mutableFloatStateOf(if (task.score > 0) task.score.toFloat() else 95f) }
    var catatan by remember { mutableStateOf(task.catatanBm) }

    val calculatedGrade = when {
        scoreValue >= 95 -> "Excellent"
        scoreValue >= 75 -> "Good"
        scoreValue >= 50 -> "Normal"
        else -> "Poor"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "REVIEW & PENILAIAN BM: ${task.areaName}",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${task.dayDateStr} | ${task.scheduleTime}",
                    fontSize = 11.sp,
                    color = SkyBlue,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Watermarked Photo Display Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyPrimary)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "FOTO & PENANDA LOKASI CS",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E293B))
                                .border(1.dp, SkyBlue, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "FOTO TERTAMPIL: ${task.areaName}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = SkyBlue,
                                        modifier = Modifier.width(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = task.photoTimestamp ?: "01/08/2026 12:30 WIB",
                                        color = Color.LightGray,
                                        fontSize = 10.sp
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = EmeraldSuccess,
                                        modifier = Modifier.width(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = task.gpsLocation ?: "Benings Glow Clinic Gresik",
                                        color = EmeraldSuccess,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Score Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nilai Akhir BM (0-100):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${scoreValue.toInt()} / 100",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SkyBlue
                    )
                }

                Slider(
                    value = scoreValue,
                    onValueChange = { scoreValue = it },
                    valueRange = 0f..100f,
                    steps = 20,
                    colors = SliderDefaults.colors(
                        thumbColor = SkyBlue,
                        activeTrackColor = SkyBlue
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Kalkulasi Grade:", fontSize = 11.sp)
                    GradeBadge(grade = calculatedGrade, score = scoreValue.toInt())
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Catatan BM Input
                OutlinedTextField(
                    value = catatan,
                    onValueChange = { catatan = it },
                    label = { Text("Catatan BM / Instruksi Revisi") },
                    placeholder = { Text("Contoh: Cermin perlu diusap ulang bagian sudut bawah...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmitReview(true, scoreValue.toInt(), calculatedGrade, catatan)
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Approve")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    onSubmitReview(false, scoreValue.toInt(), calculatedGrade, if (catatan.isEmpty()) "Perlu perbaikan kebersihan" else catatan)
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = RoseError),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Cancel, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tolak / Revisi")
            }
        }
    )
}
