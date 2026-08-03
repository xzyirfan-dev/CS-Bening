package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserEntity
import com.example.ui.theme.AmberPending
import com.example.ui.theme.BentoBlueHero
import com.example.ui.theme.BentoBlueOnHero
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardDark
import com.example.ui.theme.BentoMint
import com.example.ui.theme.BentoMintOn
import com.example.ui.theme.BentoTextMuted
import com.example.ui.theme.RoseError
import com.example.ui.theme.SkyBlue

@Composable
fun AppTopBar(
    currentUser: UserEntity?,
    currentRole: String,
    isOnline: Boolean,
    isSyncing: Boolean,
    isDarkMode: Boolean,
    unreadNotificationCount: Int,
    onLogout: () -> Unit,
    onRoleChange: (String) -> Unit,
    onToggleOnline: () -> Unit,
    onManualSync: () -> Unit,
    onToggleDarkMode: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Top Header Line: Bento Pro Header & User Profile
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "FIELDREPORT PRO • BENINGS GLOW CLINIC",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color = BentoTextMuted
                    )
                    Text(
                        text = currentUser?.name ?: "Benings Glow Clinic",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (currentUser != null) "NIK: ${currentUser.nik} • ${currentUser.title}" else "Performance Tracker",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Dark Mode Toggle Pill
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onToggleDarkMode() }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = if (isDarkMode) AmberPending else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Notification Bento Pill
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onNotificationClick() }
                            .padding(8.dp)
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotificationCount > 0) {
                                    Badge(containerColor = BentoMint, contentColor = BentoMintOn) {
                                        Text(unreadNotificationCount.toString(), fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifikasi",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Logout Button
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(RoseError.copy(alpha = 0.15f))
                            .border(1.dp, RoseError.copy(alpha = 0.3f), CircleShape)
                            .clickable { onLogout() }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Keluar Account",
                            tint = RoseError,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bento Navigation & Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Role Badge / Switcher
                if (currentUser?.role == "CS") {
                    // CS user only sees Mode CS badge, cannot access or switch to Mode BM
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(BentoBlueHero)
                            .border(1.dp, BentoBorder, RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = BentoBlueOnHero
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Mode CS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoBlueOnHero
                            )
                        }
                    }
                } else {
                    // BM user can access both Mode CS and Mode BM
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.dp, BentoBorder, RoundedCornerShape(24.dp))
                            .padding(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val csBg by animateColorAsState(
                            targetValue = if (currentRole == "CS") BentoBlueHero else Color.Transparent,
                            label = "csBg"
                        )
                        val bmBg by animateColorAsState(
                            targetValue = if (currentRole == "BM") BentoBlueHero else Color.Transparent,
                            label = "bmBg"
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(csBg)
                                .clickable { onRoleChange("CS") }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = if (currentRole == "CS") BentoBlueOnHero else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Mode CS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (currentRole == "CS") BentoBlueOnHero else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(bmBg)
                                .clickable { onRoleChange("BM") }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = if (currentRole == "BM") BentoBlueOnHero else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Mode BM",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (currentRole == "BM") BentoBlueOnHero else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Bento Real-Time Sync Indicator Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isOnline) BentoMint else AmberPending.copy(alpha = 0.2f))
                        .border(1.dp, if (isOnline) BentoMint.copy(alpha = 0.5f) else AmberPending, RoundedCornerShape(20.dp))
                        .clickable { onToggleOnline() }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(if (isOnline) BentoMintOn else AmberPending)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSyncing) "Sync..." else if (isOnline) "Realtime Sync" else "Offline",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isOnline) BentoMintOn else AmberPending
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (isSyncing) Icons.Default.Sync else if (isOnline) Icons.Default.CloudDone else Icons.Default.CloudOff,
                        contentDescription = "Sync",
                        tint = if (isOnline) BentoMintOn else AmberPending,
                        modifier = Modifier
                            .size(14.dp)
                            .clickable { onManualSync() }
                    )
                }
            }
        }
    }
}


