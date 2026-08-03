package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.SkyBlue

@Composable
fun PeriodFilterDialog(
    onDismiss: () -> Unit,
    onGenerateReport: (startDate: String, endDate: String) -> Unit
) {
    var startDate by remember { mutableStateOf("01/08/2026") }
    var endDate by remember { mutableStateOf("07/08/2026") } // Default 1 week range

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.PictureAsPdf,
                contentDescription = null,
                tint = SkyBlue
            )
        },
        title = {
            Text(
                text = "EXPORT LAPORAN PDF",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "Pilih Periode Filter (Mingguan / Bulanan) untuk meng-generate dokumen PDF resmi:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Tanggal Mulai (Start Date)") },
                    placeholder = { Text("DD/MM/YYYY") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("Tanggal Selesai (End Date)") },
                    placeholder = { Text("DD/MM/YYYY") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Preset Quick Buttons
                Text(
                    text = "Preset Cepat:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        onClick = {
                            startDate = "01/08/2026"
                            endDate = "07/08/2026"
                        }
                    ) {
                        Text("1 Minggu Pertama", fontSize = 11.sp)
                    }
                    TextButton(
                        onClick = {
                            startDate = "01/08/2026"
                            endDate = "31/08/2026"
                        }
                    ) {
                        Text("Full 1 Bulan (Agustus)", fontSize = 11.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onGenerateReport(startDate, endDate) },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Generate PDF")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
