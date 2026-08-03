package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.NotificationEntity
import com.example.ui.theme.SkyBlue

@Composable
fun NotificationDrawerDialog(
    notifications: List<NotificationEntity>,
    onDismiss: () -> Unit,
    onNotificationClick: (Long) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = SkyBlue
            )
        },
        title = {
            Text(text = "NOTIFIKASI PUSH REAL-TIME", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (notifications.isEmpty()) {
                    Text(text = "Belum ada notifikasi baru.", fontSize = 12.sp)
                } else {
                    LazyColumn(
                        modifier = Modifier.height(280.dp)
                    ) {
                        items(notifications) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { onNotificationClick(item.id) },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (item.isRead) MaterialTheme.colorScheme.surfaceVariant else SkyBlue.copy(alpha = 0.15f)
                                )
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = item.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = item.message,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.timestampStr,
                                        fontSize = 9.sp,
                                        color = SkyBlue
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}
