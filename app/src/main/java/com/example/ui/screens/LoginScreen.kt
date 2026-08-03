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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun LoginScreen(
    onLoginSubmit: (String, String, (Boolean, String?) -> Unit) -> Unit
) {
    var usernameInput by remember { mutableStateOf("BM01") }
    var passwordInput by remember { mutableStateOf("Gresik123") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Branding
            Text(
                text = "FIELDREPORT PRO • BENINGS GLOW CLINIC",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.2.sp,
                color = BentoTextMuted
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Login Karyawan & Audit",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Sistem Evaluasi Kebersihan & Manajemen Jadwal CS",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Main Bento Login Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BentoBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "MASUKKAN AKUN",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        color = SkyBlue
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Username Field
                    OutlinedTextField(
                        value = usernameInput,
                        onValueChange = {
                            usernameInput = it
                            errorMessage = null
                        },
                        label = { Text("Username / User ID", fontSize = 12.sp) },
                        placeholder = { Text("Contoh: BM01 / CS01") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = SkyBlue)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SkyBlue,
                            unfocusedBorderColor = BentoBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password Field
                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = {
                            passwordInput = it
                            errorMessage = null
                        },
                        label = { Text("Password", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = SkyBlue)
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle Password"
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SkyBlue,
                            unfocusedBorderColor = BentoBorder
                        )
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = RoseError,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Login Action Button
                    Button(
                        onClick = {
                            isLoading = true
                            onLoginSubmit(usernameInput, passwordInput) { success, msg ->
                                isLoading = false
                                if (!success) {
                                    errorMessage = msg
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BentoBlueHero)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Login,
                                contentDescription = null,
                                tint = BentoBlueOnHero
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isLoading) "MEMPROSES..." else "MASUK SISTEM",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = BentoBlueOnHero
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Demo Account Selector (Direct 1-Click Fill)
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "PILIH AKUN LOGIN CEPAT (Sesuai Spreadsheet):",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.8.sp,
                    color = BentoTextMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Account 1: BM01 - Fitria Nor Istiqomah
                QuickAccountCard(
                    userTag = "BM01",
                    nik = "2600701036",
                    name = "Fitria Nor Istiqomah",
                    jabatan = "Manager Clinic Gresik (BM)",
                    badgeColor = BentoMint,
                    badgeTextColor = BentoMintOn,
                    onClick = {
                        usernameInput = "BM01"
                        passwordInput = "Gresik123"
                        onLoginSubmit("BM01", "Gresik123") { _, _ -> }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Account 2: CS01 - Mohammad Rangga adinata
                QuickAccountCard(
                    userTag = "CS01",
                    nik = "2600713030",
                    name = "Mohammad Rangga adinata",
                    jabatan = "Cleaning Service (CS)",
                    badgeColor = SkyBlue.copy(alpha = 0.2f),
                    badgeTextColor = SkyBlue,
                    onClick = {
                        usernameInput = "CS01"
                        passwordInput = "Gresik123"
                        onLoginSubmit("CS01", "Gresik123") { _, _ -> }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Account 3: CS02 - SAMPURNA
                QuickAccountCard(
                    userTag = "CS02",
                    nik = "2400713043",
                    name = "SAMPURNA",
                    jabatan = "Cleaning Service / Security (CS)",
                    badgeColor = SkyBlue.copy(alpha = 0.2f),
                    badgeTextColor = SkyBlue,
                    onClick = {
                        usernameInput = "CS02"
                        passwordInput = "Gresik123"
                        onLoginSubmit("CS02", "Gresik123") { _, _ -> }
                    }
                )
            }
        }
    }
}

@Composable
private fun QuickAccountCard(
    userTag: String,
    nik: String,
    name: String,
    jabatan: String,
    badgeColor: Color,
    badgeTextColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BentoBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeColor)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = userTag,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = badgeTextColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "NIK: $nik • $jabatan",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Login",
                    tint = BentoMint,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
