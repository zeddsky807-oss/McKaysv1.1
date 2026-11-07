package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.CircularProgressIndicator
import com.example.ui.theme.Gold500
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.McKaysViewModel
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Rose500
import com.example.ui.theme.RoyalBlue500
import com.example.ui.theme.RoyalBlue900
import com.example.util.ExportHelper

@Composable
fun ProfileTab(
    viewModel: McKaysViewModel,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState()
    val syncState by viewModel.syncState.collectAsState()
    val branchesAndStaff by viewModel.branchesAndStaff.collectAsState()
    val stockItems by viewModel.stockItems.collectAsState()
    val stockRequests by viewModel.stockRequests.collectAsState()
    val members by viewModel.members.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val dailyOmset by viewModel.dailyOmset.collectAsState()

    val isAdmin = currentUser.role == UserRole.ADMIN && currentUser.email.trim().equals("budayaxwel@gmail.com", ignoreCase = true)

    var showManageBranchesDialog by remember { mutableStateOf(false) }
    var showAddBranchStaffDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Akun & Pengaturan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // Profile Card
        item {
            GlassmorphicCard(
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .clip(CircleShape)
                            .background(RoyalBlue900),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = currentUser.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentUser.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isAdmin) Emerald500.copy(alpha = 0.15f) else RoyalBlue500.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Default.Shield,
                                contentDescription = null,
                                tint = if (isAdmin) Emerald500 else RoyalBlue500,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isAdmin) "Role: Admin (Kontrol Penuh)" else "Role: Staff Cabang",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isAdmin) Emerald500 else RoyalBlue500
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentUser.branchName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Role Switcher Card (Convenience to test Admin vs Staff behaviors)
        item {
            GlassmorphicCard(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mode Role: ${if (isAdmin) "Administrator" else "Staff Kasir"}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isAdmin) "Akses penuh gudang pusat & ekspor data" else "Akses kasir cabang, tombol ekspor dihilangkan",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                    Button(
                        onClick = {
                            if (isAdmin) {
                                viewModel.switchRole(UserRole.STAFF)
                                Toast.makeText(context, "Beralih ke akun Staff Cabang", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.switchRole(UserRole.ADMIN)
                                Toast.makeText(context, "Beralih ke akun Admin Pusat", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isAdmin) RoyalBlue500 else RoyalBlue900),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("switch_role_toggle_button")
                    ) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isAdmin) "Switch Staff" else "Switch Admin", fontSize = 11.sp)
                    }
                }
            }
        }

        // =======================================================
        // ADMIN ONLY: Manajemen Cabang & Staff oleh Admin
        // =======================================================
        if (isAdmin) {
            item {
                Text(
                    text = "Manajemen Cabang & Staff (Admin)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                GlassmorphicCard(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showManageBranchesDialog = true }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Rose500.copy(alpha = 0.12f),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.PersonRemove, contentDescription = null, tint = Rose500)
                                    }
                                }
                                Column {
                                    Text(
                                        text = "Hapus / Kelola Akun Cabang & Staff",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Admin memiliki hak penuh menghapus toko cabang / staff",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Divider()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showAddBranchStaffDialog = true }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = RoyalBlue900.copy(alpha = 0.12f),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Add, contentDescription = null, tint = RoyalBlue900)
                                    }
                                }
                                Column {
                                    Text(
                                        text = "Tambah Cabang atau Staff Baru",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Daftarkan toko cabang baru atau akun staff kasir",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            // =======================================================
            // ADMIN ONLY: Sinkronisasi Real-Time Google Drive ke budayaxwel@gmail.com
            // =======================================================
            item {
                Text(
                    text = "Google Drive Cloud Sync (Real-Time)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                GlassmorphicCard(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_google_drive_sync_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = RoyalBlue500.copy(alpha = 0.15f),
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Default.CloudSync,
                                            contentDescription = "Google Drive",
                                            tint = RoyalBlue500
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = "Google Drive Sync",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Akun: budayaxwel@gmail.com",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (syncState.isSyncing) Gold500.copy(alpha = 0.15f) else Emerald500.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    if (syncState.isSyncing) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(12.dp),
                                            color = Gold500,
                                            strokeWidth = 2.dp
                                        )
                                        Text("Sinkronisasi...", fontSize = 11.sp, color = Gold500, fontWeight = FontWeight.Bold)
                                    } else {
                                        Icon(Icons.Default.CloudDone, contentDescription = null, tint = Emerald500, modifier = Modifier.size(14.dp))
                                        Text("Real-Time Aktif", fontSize = 11.sp, color = Emerald500, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Status: ${syncState.lastSyncStatusMessage}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Waktu Sinkron Terakhir: ${syncState.lastSyncFormatted}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Berkas Cloud: ${syncState.syncedFiles.joinToString(", ")}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = RoyalBlue500,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Otomatis Singkronkan Perubahan", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                Text("Setiap transaksi kasir & update stok langsung disingkronkan", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
                            }
                            Switch(
                                checked = syncState.isAutoSyncEnabled,
                                onCheckedChange = { viewModel.toggleAutoSync(it) }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.triggerGoogleDriveSync()
                                    Toast.makeText(context, "Data berhasil disinkronkan real-time ke Google Drive (budayaxwel@gmail.com)!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue900),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("manual_drive_sync_button")
                            ) {
                                Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Sinkronkan Sekarang", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    viewModel.openOrShareToGoogleDrive(context)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("open_drive_backup_button")
                            ) {
                                Icon(Icons.Default.FolderShared, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Simpan ke Drive", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // =======================================================
            // ADMIN ONLY: Ekspor Data Modern (Excel, CSV, PDF)
            // (DIHILANGKAN DARI AKUN STAFF)
            // =======================================================
            item {
                Text(
                    text = "Ekspor Data Modern (Eksklusif Admin)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                GlassmorphicCard(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_export_section")
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // 1. Excel Format
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    ExportHelper.exportToExcel(
                                        context = context,
                                        stockList = stockItems,
                                        requests = stockRequests,
                                        members = members,
                                        transactions = transactions,
                                        dailyOmset = dailyOmset
                                    )
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Emerald500.copy(alpha = 0.12f),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.TableChart, contentDescription = null, tint = Emerald500)
                                    }
                                }
                                Column {
                                    Text(
                                        text = "Ekspor Data (Format Excel Modern)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Laporan stok, omset, dan order kompatibel Excel/Sheets",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Divider()

                        // 2. CSV Format
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    ExportHelper.exportToCsv(
                                        context = context,
                                        stockList = stockItems,
                                        requests = stockRequests,
                                        dailyOmset = dailyOmset
                                    )
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = RoyalBlue500.copy(alpha = 0.12f),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.ListAlt, contentDescription = null, tint = RoyalBlue500)
                                    }
                                }
                                Column {
                                    Text(
                                        text = "Ekspor Data (Format CSV)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Format tabel data CSV terstruktur",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Divider()

                        // 3. PDF Format
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    ExportHelper.exportToPdfSummary(
                                        context = context,
                                        stockList = stockItems,
                                        requests = stockRequests,
                                        members = members,
                                        dailyOmset = dailyOmset
                                    )
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Rose500.copy(alpha = 0.12f),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Rose500)
                                    }
                                }
                                Column {
                                    Text(
                                        text = "Ekspor Data (Format PDF Dokumen)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Dokumen cetak ringkasan resmi admin",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        } else {
            // =======================================================
            // STAFF CABANG: Ekspor data & Google Drive DIHILANGKAN
            // Tampilkan kartu informasi keamanan staff
            // =======================================================
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("staff_restricted_notice")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = RoyalBlue500.copy(alpha = 0.15f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = RoyalBlue500)
                            }
                        }
                        Column {
                            Text(
                                text = "Akses Staff Kasir Cabang",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Tombol ekspor data file (Excel/CSV/PDF) dan sinkronisasi Google Drive sengaja dihilangkan dari akun staff dan hanya tersedia eksklusif pada akun Admin (budayaxwel@gmail.com).",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Logout Card
        item {
            GlassmorphicCard(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLogout() }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Rose500.copy(alpha = 0.12f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Logout, contentDescription = null, tint = Rose500)
                            }
                        }
                        Text(
                            text = "Keluar / Logout Akun",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Rose500
                        )
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Rose500)
                }
            }
        }

        // App Version
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "McKays. V.1.1",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "One UI Design • Sistem Inventaris & Kasir Parfum Modern",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontSize = 10.sp
                )
            }
        }

        // Bottom space
        item {
            Spacer(modifier = Modifier.height(120.dp))
        }
    }

    // Dialog: Hapus Akun Cabang atau Staff oleh Admin
    if (showManageBranchesDialog) {
        AlertDialog(
            onDismissRequest = { showManageBranchesDialog = false },
            title = {
                Text("Hapus Akun Cabang atau Staff", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Admin memiliki otoritas penuh menghapus toko cabang atau akun staff dari sistem.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    if (branchesAndStaff.isEmpty()) {
                        Text(
                            text = "Tidak ada akun cabang atau staff tersisa.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        branchesAndStaff.forEach { bs ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = bs.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${bs.type} • ${bs.email}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        viewModel.deleteBranchOrStaff(bs.id)
                                        Toast.makeText(context, "${bs.title} berhasil dihapus dari sistem!", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Rose500)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showManageBranchesDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue900)
                ) {
                    Text("Tutup")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    // Dialog: Tambah Cabang / Staff
    if (showAddBranchStaffDialog) {
        var title by remember { mutableStateOf("") }
        var type by remember { mutableStateOf("Cabang") }
        var email by remember { mutableStateOf("") }
        var detail by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddBranchStaffDialog = false },
            title = { Text("Tambah Akun Cabang / Staff", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { type = "Cabang" },
                            colors = ButtonDefaults.buttonColors(containerColor = if (type == "Cabang") RoyalBlue900 else MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Toko Cabang", color = if (type == "Cabang") Color.White else MaterialTheme.colorScheme.onSurface)
                        }
                        Button(
                            onClick = { type = "Staff" },
                            colors = ButtonDefaults.buttonColors(containerColor = if (type == "Staff") RoyalBlue900 else MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Staff Kasir", color = if (type == "Staff") Color.White else MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(if (type == "Cabang") "Nama Cabang Toko" else "Nama Lengkap Staff") },
                        placeholder = { Text(if (type == "Cabang") "Misal: Cabang Grand Indonesia" else "Misal: Staff Kasir: Dimas") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Login Akun") },
                        placeholder = { Text("nama@mckays.com") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = detail,
                        onValueChange = { detail = it },
                        label = { Text("Lokasi / Penempatan") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            viewModel.addBranchOrStaff(
                                title = title.trim(),
                                type = type,
                                email = email.trim().ifBlank { "staff@mckays.com" },
                                detail = detail.trim().ifBlank { "Lokasi Cabang" }
                            )
                            showAddBranchStaffDialog = false
                            Toast.makeText(context, "$title berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue900),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddBranchStaffDialog = false }) {
                    Text("Batal")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}
