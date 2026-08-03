package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.SkyBlue

@Composable
fun TaskCompletionBarChart(
    dailyCompletionCounts: List<Int>, // Max 22 per day
    daysLabels: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "GRAFIK PENYELESAIAN TUGAS HARIAN (MAX 22 TUGAS/HARI)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SkyBlue
            )
            Text(
                text = "Capaian Sesi 1 & Sesi 2 Benings Glow Clinic Gresik",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            val maxVal = 22f
            val barColor = SkyBlue

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                val width = size.width
                val height = size.height
                val barCount = dailyCompletionCounts.size.coerceAtLeast(1)
                val barWidth = (width / barCount) * 0.5f
                val spacing = (width / barCount) * 0.5f

                // Draw background grid lines
                for (i in 0..4) {
                    val y = height - (height * (i / 4f))
                    drawLine(
                        color = Color.Gray.copy(alpha = 0.2f),
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1f
                    )
                }

                // Draw bars
                dailyCompletionCounts.forEachIndexed { index, count ->
                    val barHeight = (count / maxVal) * height
                    val x = index * (barWidth + spacing) + spacing / 2
                    val y = height - barHeight

                    drawRect(
                        color = barColor,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Days Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                daysLabels.forEach { label ->
                    Text(text = label, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun UserRetentionTrendChart(
    retentionData: List<Float> // 0f to 100f values
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ANALITIK RETENSI PETUGAS & TINGKAT KEPATUHAN (%)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldSuccess
            )
            Text(
                text = "Tren Keaktifan Login & Ketepatan Waktu Upload Timestamp",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                val width = size.width
                val height = size.height

                val path = Path()
                if (retentionData.isNotEmpty()) {
                    val stepX = width / (retentionData.size - 1).coerceAtLeast(1)

                    retentionData.forEachIndexed { index, percent ->
                        val x = index * stepX
                        val y = height - ((percent / 100f) * height)

                        if (index == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }

                        drawCircle(
                            color = EmeraldSuccess,
                            radius = 4.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }

                    drawPath(
                        path = path,
                        color = EmeraldSuccess,
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(16.dp)
                        .background(EmeraldSuccess, shape = RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Retensi Pengguna Aktif Harian: 100% (Konsisten Sesuai Shift)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
