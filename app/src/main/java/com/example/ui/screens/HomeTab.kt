package com.example.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StockRequestStatus
import com.example.data.model.UserRole
import com.example.ui.McKaysViewModel
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.GradientCard
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Gold500
import com.example.ui.theme.RoyalBlue500
import com.example.ui.theme.RoyalBlue900
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeTab(
    viewModel: McKaysViewModel,
    onNavigateTab: (Int) -> Unit
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState()
    val dailyOmset by viewModel.dailyOmset.collectAsState()
    val selectedDateMillis by viewModel.selectedDateMillis.collectAsState()
    val stockItems by viewModel.stockItems.collectAsState()
    val stockRequests by viewModel.stockRequests.collectAsState()
    val members by viewModel.members.collectAsState()
    val branchesAndStaff by viewModel.branchesAndStaff.collectAsState()

    val isAdmin = currentUser.role == UserRole.ADMIN

    var showEditOmsetDialog by remember { mutableStateOf(false) }

    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply { maximumFractionDigits = 0 }
    }

    val calendarFormat = remember {
        SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
    }

    val pendingRequestsCount = stockRequests.count { it.status == StockRequestStatus.PENDING_ADMIN }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Header: Greeting & Role Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Halo, ${currentUser.email}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isAdmin) Emerald500 else RoyalBlue500)
                        )
                        Text(
                            text = if (isAdmin) "Akses: Gudang Stok Pusat (Admin)" else "Akses: ${currentUser.branchName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isAdmin) Emerald500 else RoyalBlue500,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "V.1.1",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // 1. Kalender Masehi Card
        item {
            GlassmorphicCard(
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = RoyalBlue900.copy(alpha = 0.1f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.CalendarMonth,
                                    contentDescription = "Kalender Masehi",
                                    tint = RoyalBlue900
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Tanggal Kalender Masehi:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = calendarFormat.format(Date(selectedDateMillis)),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val calendar = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    val newCal = Calendar.getInstance().apply {
                                        set(year, month, dayOfMonth)
                                    }
                                    viewModel.setSelectedDate(newCal.timeInMillis)
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue900),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("pick_date_button")
                    ) {
                        Text("Pilih Tanggal", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // 2. Omset Harian Real-Time Card
        item {
            GradientCard(
                gradientColors = listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6)),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("daily_omset_card")
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Omset Harian Real-Time",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }

                        IconButton(
                            onClick = { showEditOmsetDialog = true },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .testTag("edit_omset_button")
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Ubah Omset",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = currencyFormat.format(dailyOmset),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Terupdate otomatis setiap kali ada transaksi kasir atau dapat disesuaikan manual.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.80f),
                        fontSize = 11.sp
                    )
                }
            }
        }

        // 3. Quick Performance Metrics (2x2 Grid)
        item {
            Text(
                text = "Ringkasan Operasional",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Total Varian Stok",
                        value = "${stockItems.size} Item",
                        subtitle = "${stockItems.sumOf { it.stockQuantity }} botol fisik",
                        icon = Icons.Default.Inventory2,
                        iconTint = RoyalBlue500,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateTab(1) }
                    )
                    MetricCard(
                        title = "Request Stok",
                        value = "$pendingRequestsCount Pending",
                        subtitle = if (isAdmin) "Perlu tinjauan admin" else "Menunggu admin",
                        icon = Icons.Default.LocalShipping,
                        iconTint = if (pendingRequestsCount > 0) Gold500 else Emerald500,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateTab(2) }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Member Terdaftar",
                        value = "${members.size} Pelanggan",
                        subtitle = "Loyalitas & reward",
                        icon = Icons.Default.CardMembership,
                        iconTint = Color(0xFF8B5CF6),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateTab(3) }
                    )
                    MetricCard(
                        title = "Cabang & Staff",
                        value = "${branchesAndStaff.size} Terdaftar",
                        subtitle = "Manajemen toko",
                        icon = Icons.Default.Storefront,
                        iconTint = Emerald500,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateTab(4) }
                    )
                }
            }
        }

        // 4. Quick Actions
        item {
            Text(
                text = "Menu Cepat",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionButton(
                    label = "Buka Kasir",
                    icon = Icons.Default.PointOfSale,
                    color = RoyalBlue900,
                    modifier = Modifier.weight(1f)
                ) { onNavigateTab(1) }

                QuickActionButton(
                    label = if (isAdmin) "Tinjau Order" else "Order Stok",
                    icon = Icons.Default.LocalShipping,
                    color = if (isAdmin) Emerald500 else RoyalBlue500,
                    modifier = Modifier.weight(1f)
                ) { onNavigateTab(2) }

                QuickActionButton(
                    label = "Member",
                    icon = Icons.Default.CardMembership,
                    color = Gold500,
                    modifier = Modifier.weight(1f)
                ) { onNavigateTab(3) }
            }
        }

        // Bottom spacing for floating navigation bar
        item {
            Spacer(modifier = Modifier.height(110.dp))
        }
    }

    // Dialog Ubah Nilai Omset Harian
    if (showEditOmsetDialog) {
        var inputOmset by remember { mutableStateOf(dailyOmset.toLong().toString()) }
        AlertDialog(
            onDismissRequest = { showEditOmsetDialog = false },
            title = { Text("Ubah Nilai Omset Harian", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Sesuaikan nilai omset harian secara manual untuk tanggal ${calendarFormat.format(Date(selectedDateMillis))}.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = inputOmset,
                        onValueChange = { inputOmset = it },
                        label = { Text("Omset Baru (Rp)") },
                        prefix = { Text("Rp ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_edit_omset")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = inputOmset.toDoubleOrNull() ?: dailyOmset
                        viewModel.updateDailyOmset(parsed)
                        showEditOmsetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue900),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("save_omset_button")
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditOmsetDialog = false }) {
                    Text("Batal")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    GlassmorphicCard(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = iconTint.copy(alpha = 0.12f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
        }
    }
}

@Composable
private fun QuickActionButton(
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(26.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}
