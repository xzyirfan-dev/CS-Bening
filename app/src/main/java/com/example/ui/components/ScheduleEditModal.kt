package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.ChecklistTaskEntity
import com.example.ui.theme.BentoBlueHero
import com.example.ui.theme.BentoBlueOnHero
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardDark
import com.example.ui.theme.BentoMint
import com.example.ui.theme.BentoMintOn
import com.example.ui.theme.BentoTextMuted
import com.example.ui.theme.SkyBlue

@Composable
fun ScheduleEditModal(
    taskToEdit: ChecklistTaskEntity?,
    isAddMode: Boolean,
    selectedDayNum: Int,
    selectedMonth: String,
    onDismiss: () -> Unit,
    onSaveUpdate: (taskId: Long, areaName: String, scheduleTime: String, estimateMinutes: Int, session: String, picName: String) -> Unit,
    onSaveAdd: (dayDateStr: String, dayNum: Int, monthYearStr: String, session: String, areaName: String, estimateMinutes: Int, scheduleTime: String, picName: String) -> Unit
) {
    val dayOfWeekList = listOf("Sabtu", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat")
    val dayName = dayOfWeekList[(selectedDayNum - 1) % 7]
    val dayFormatted = String.format("%02d", selectedDayNum)
    val defaultDateStr = "$dayName, $dayFormatted $selectedMonth"

    var areaNameInput by remember { mutableStateOf(taskToEdit?.areaName ?: "LANTAI") }
    var scheduleTimeInput by remember { mutableStateOf(taskToEdit?.scheduleTime ?: "11:00 - 11:30") }
    var estimateMinutesInput by remember { mutableStateOf(taskToEdit?.estimateMinutes?.toString() ?: "30") }
    var sessionInput by remember {
        mutableStateOf(
            taskToEdit?.session ?: "SESI 1 (Sebelum Istirahat: 11:00 - 15:00)"
        )
    }
    var picNameInput by remember { mutableStateOf(taskToEdit?.picName ?: "Mohammad Rangga adinata") }

    val presetAreas = listOf("LANTAI", "WC", "WASTAFEL", "CERMIN", "DINDING", "TRASH", "KESET", "KRAN AIR", "AROMA", "CALMIC", "TANAMAN")
    val presetPics = listOf("Mohammad Rangga adinata", "SAMPURNA", "Fitria Nor Istiqomah")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BentoBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(BentoBlueHero)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = BentoBlueOnHero,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isAddMode) "TAMBAH JADWAL BARU" else "EDIT JADWAL CS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                color = SkyBlue
                            )
                            Text(
                                text = defaultDateStr,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Area Name Input & Presets
                Text(
                    text = "NAMA AREA / INVENTARIS:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BentoTextMuted
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = areaNameInput,
                    onValueChange = { areaNameInput = it },
                    leadingIcon = { Icon(imageVector = Icons.Default.Place, contentDescription = null, tint = SkyBlue) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SkyBlue,
                        unfocusedBorderColor = BentoBorder
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                // Quick Area Presets Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presetAreas.take(5).forEach { area ->
                        val isSelected = areaNameInput == area
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) BentoBlueHero else MaterialTheme.colorScheme.surface)
                                .border(1.dp, if (isSelected) BentoBlueHero else BentoBorder, RoundedCornerShape(10.dp))
                                .clickable { areaNameInput = area }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = area,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) BentoBlueOnHero else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Schedule Time & Duration
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(modifier = Modifier.weight(1.5f)) {
                        Text(
                            text = "JADWAL JAM:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoTextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = scheduleTimeInput,
                            onValueChange = { scheduleTimeInput = it },
                            leadingIcon = { Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = SkyBlue) },
                            placeholder = { Text("11:00 - 11:30") },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SkyBlue,
                                unfocusedBorderColor = BentoBorder
                            )
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ESTIMASI (MIN):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoTextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = estimateMinutesInput,
                            onValueChange = { estimateMinutesInput = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SkyBlue,
                                unfocusedBorderColor = BentoBorder
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Session Selector
                Text(
                    text = "SESI KERJA:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BentoTextMuted
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val s1 = "SESI 1 (Sebelum Istirahat: 11:00 - 15:00)"
                    val s2 = "SESI 2 (Setelah Istirahat: 16:00 - 19:00)"

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (sessionInput == s1) BentoBlueHero else MaterialTheme.colorScheme.surface)
                            .border(1.dp, if (sessionInput == s1) BentoBlueHero else BentoBorder, RoundedCornerShape(12.dp))
                            .clickable { sessionInput = s1 }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SESI 1 (11:00-15:00)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (sessionInput == s1) BentoBlueOnHero else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (sessionInput == s2) BentoBlueHero else MaterialTheme.colorScheme.surface)
                            .border(1.dp, if (sessionInput == s2) BentoBlueHero else BentoBorder, RoundedCornerShape(12.dp))
                            .clickable { sessionInput = s2 }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SESI 2 (16:00-19:00)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (sessionInput == s2) BentoBlueOnHero else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Assigned PIC Selector
                Text(
                    text = "PETUGAS PIC KERJA:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BentoTextMuted
                )
                Spacer(modifier = Modifier.height(6.dp))

                presetPics.forEach { pic ->
                    val isPicSelected = picNameInput == pic
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isPicSelected) BentoMint else MaterialTheme.colorScheme.surface)
                            .border(1.dp, if (isPicSelected) BentoMint else BentoBorder, RoundedCornerShape(12.dp))
                            .clickable { picNameInput = pic }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = if (isPicSelected) BentoMintOn else SkyBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = pic,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isPicSelected) BentoMintOn else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Save Action Button
                Button(
                    onClick = {
                        val est = estimateMinutesInput.toIntOrNull() ?: 30
                        if (isAddMode) {
                            onSaveAdd(
                                defaultDateStr,
                                selectedDayNum,
                                selectedMonth,
                                sessionInput,
                                areaNameInput.uppercase(),
                                est,
                                scheduleTimeInput,
                                picNameInput
                            )
                        } else if (taskToEdit != null) {
                            onSaveUpdate(
                                taskToEdit.id,
                                areaNameInput.uppercase(),
                                scheduleTimeInput,
                                est,
                                sessionInput,
                                picNameInput
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BentoMint)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null, tint = BentoMintOn)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAddMode) "SIMPAN JADWAL BARU" else "SIMPAN PERUBAHAN JADWAL",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = BentoMintOn
                        )
                    }
                }
            }
        }
    }
}
