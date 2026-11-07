package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StockRequest
import com.example.data.model.StockRequestStatus
import com.example.data.model.UserRole
import com.example.ui.McKaysViewModel
import com.example.ui.components.GlassmorphicCard
import com.example.ui.dialogs.CreateOrderDialog
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Gold500
import com.example.ui.theme.Rose500
import com.example.ui.theme.RoyalBlue500
import com.example.ui.theme.RoyalBlue900

@Composable
fun OrderTab(
    viewModel: McKaysViewModel
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState()
    val stockRequests by viewModel.stockRequests.collectAsState()

    val isAdmin = currentUser.role == UserRole.ADMIN

    var showCreateDialog by remember { mutableStateOf(false) }
    var statusFilter by remember { mutableStateOf<StockRequestStatus?>(null) } // null = all

    val filteredRequests = stockRequests.filter { req ->
        statusFilter == null || req.status == statusFilter
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pengadaan Request Order Stok",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Alur pengadaan manual: Cabang request -> Admin setujui manual -> Cabang terima stok.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Action Button: Buat Request Stok Baru
        item {
            Button(
                onClick = { showCreateDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue900),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("create_stock_request_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Buat Request Stok Cabang Baru",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Info Banner
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isAdmin) Emerald500.copy(alpha = 0.12f) else RoyalBlue500.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        if (isAdmin) Icons.Default.CheckCircle else Icons.Default.Info,
                        contentDescription = null,
                        tint = if (isAdmin) Emerald500 else RoyalBlue500,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = if (isAdmin) {
                            "Admin Panel: Request order dari cabang tidak otomatis masuk inventaris. Anda harus meninjau dan menyetujuinya secara manual di bawah ini."
                        } else {
                            "Staff Cabang Panel: Setelah request dibuat, pesanan berstatus 'Menunggu Persetujuan Admin'. Anda baru dapat menerima stok fisik setelah admin menyetujuinya."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isAdmin) Emerald500 else RoyalBlue500,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Status Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    null to "Semua (${stockRequests.size})",
                    StockRequestStatus.PENDING_ADMIN to "Menunggu",
                    StockRequestStatus.APPROVED_ADMIN to "Disetujui",
                    StockRequestStatus.RECEIVED_BRANCH to "Diterima"
                ).forEach { (status, label) ->
                    val isSelected = statusFilter == status
                    FilterChip(
                        selected = isSelected,
                        onClick = { statusFilter = status },
                        label = { Text(label, fontSize = 11.sp) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Empty state
        if (filteredRequests.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Tidak ada order pengadaan stok",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Gunakan tombol 'Buat Request Stok Cabang Baru' untuk memulai.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Order Request Cards
        items(filteredRequests, key = { it.id }) { req ->
            OrderRequestCard(
                request = req,
                isAdmin = isAdmin,
                onApprove = {
                    viewModel.approveStockRequest(req.id)
                    Toast.makeText(context, "Order ${req.id} disetujui secara manual! Cabang kini dapat menerima stok.", Toast.LENGTH_SHORT).show()
                },
                onReject = {
                    viewModel.rejectStockRequest(req.id)
                    Toast.makeText(context, "Order ${req.id} ditolak oleh Admin.", Toast.LENGTH_SHORT).show()
                },
                onReceiveStock = {
                    val success = viewModel.receiveStockAtBranch(req.id)
                    if (success) {
                        Toast.makeText(context, "Stok fisik sebesar ${req.quantity} pcs berhasil ditambahkan ke inventaris cabang!", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }

        // Space for floating bottom bar
        item {
            Spacer(modifier = Modifier.height(110.dp))
        }
    }

    // Dialog Create Request
    if (showCreateDialog) {
        CreateOrderDialog(
            initialBranchName = currentUser.branchName,
            onDismiss = { showCreateDialog = false },
            onSubmit = { branch, item, ml, qty, notes ->
                viewModel.createStockRequest(branch, item, ml, qty, notes)
                showCreateDialog = false
                Toast.makeText(context, "Request pengadaan berhasil dikirim ke Admin Pusat!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
private fun OrderRequestCard(
    request: StockRequest,
    isAdmin: Boolean,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onReceiveStock: () -> Unit
) {
    val statusColor = when (request.status) {
        StockRequestStatus.PENDING_ADMIN -> Gold500
        StockRequestStatus.APPROVED_ADMIN -> Emerald500
        StockRequestStatus.RECEIVED_BRANCH -> RoyalBlue500
        StockRequestStatus.REJECTED -> Rose500
    }

    GlassmorphicCard(
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Request ID & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = RoyalBlue900.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = request.id,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = RoyalBlue900,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                    Text(
                        text = request.branchName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = when (request.status) {
                            StockRequestStatus.PENDING_ADMIN -> "Menunggu Persetujuan Admin"
                            StockRequestStatus.APPROVED_ADMIN -> "Disetujui Admin"
                            StockRequestStatus.RECEIVED_BRANCH -> "Stok Diterima Cabang"
                            StockRequestStatus.REJECTED -> "Ditolak"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Item Details with Bottle Size Badge (10ml, 15ml, 20ml, 30ml, 50ml, 100ml)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF1E3A8A).copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(Icons.Default.LocalDrink, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFF1E3A8A))
                        Text(
                            text = "${request.ml}ml",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A)
                        )
                    }
                }
                Text(
                    text = "${request.itemName} x ${request.quantity} pcs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (request.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Catatan: ${request.notes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Diminta pada: ${request.requestedAt}${request.approvedAt?.let { " • Disetujui: $it" } ?: ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ================= Action Buttons based on Role & Status =================
            // 1. ADMIN ACTION: Manual Approval
            if (isAdmin && request.status == StockRequestStatus.PENDING_ADMIN) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admin_approve_button_${request.id}")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Terima / Setujui Order Manual", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onReject,
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Rose500, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tolak", color = Rose500, fontSize = 12.sp)
                    }
                }
            }

            // 2. STAFF / BRANCH ACTION: Receive Stock after Admin Approval
            if (request.status == StockRequestStatus.APPROVED_ADMIN) {
                Button(
                    onClick = onReceiveStock,
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue900),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("branch_receive_stock_button_${request.id}")
                ) {
                    Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Terima Stok Cabang (Tambah Fisik ke Inventaris)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // 3. Informational status if staff is waiting for admin
            if (!isAdmin && request.status == StockRequestStatus.PENDING_ADMIN) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.HourglassTop, contentDescription = null, tint = Gold500, modifier = Modifier.size(16.dp))
                    Text(
                        text = "Menunggu persetujuan manual Admin Pusat sebelum stok dapat diterima cabang.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Gold500,
                        fontSize = 11.sp
                    )
                }
            }

            // 4. Finished state
            if (request.status == StockRequestStatus.RECEIVED_BRANCH) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald500, modifier = Modifier.size(16.dp))
                    Text(
                        text = "Stok fisik sudah masuk ke inventaris cabang.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Emerald500,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
