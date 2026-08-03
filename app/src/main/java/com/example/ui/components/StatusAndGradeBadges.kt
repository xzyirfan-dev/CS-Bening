package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberPending
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseError
import com.example.ui.theme.SkyBlue

@Composable
fun StatusBadge(status: String) {
    val (bgColor, textColor) = when (status) {
        "Selesai" -> Pair(EmeraldSuccess.copy(alpha = 0.15f), EmeraldSuccess)
        "Revisi" -> Pair(RoseError.copy(alpha = 0.15f), RoseError)
        else -> Pair(AmberPending.copy(alpha = 0.15f), AmberPending)
    }

    Box(
        modifier = Modifier
            .background(bgColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.uppercase(),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GradeBadge(grade: String, score: Int = 0) {
    val (bgColor, textColor, label) = when {
        grade == "Excellent" || score >= 95 -> Triple(EmeraldSuccess, Color.White, "🟢 EXCELLENT ($score)")
        grade == "Good" || score >= 75 -> Triple(SkyBlue, Color.White, "🔵 GOOD ($score)")
        grade == "Normal" || score >= 50 -> Triple(AmberPending, Color.White, "🟡 NORMAL ($score)")
        grade == "Poor" -> Triple(RoseError, Color.White, "🔴 POOR ($score)")
        else -> Triple(Color.Gray.copy(alpha = 0.2f), Color.Gray, "BELUM DINILAI")
    }

    Box(
        modifier = Modifier
            .background(bgColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
