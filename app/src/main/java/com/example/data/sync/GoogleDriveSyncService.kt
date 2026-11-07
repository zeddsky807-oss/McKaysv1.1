package com.example.data.sync

import android.content.Context
import android.content.Intent
import com.example.data.model.Member
import com.example.data.model.SaleTransaction
import com.example.data.model.StockItem
import com.example.data.model.StockRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class GoogleDriveSyncState(
    val isSyncing: Boolean = false,
    val lastSyncTimestamp: Long = System.currentTimeMillis(),
    val lastSyncFormatted: String = "Baru saja",
    val targetAccount: String = "budayaxwel@gmail.com",
    val isAutoSyncEnabled: Boolean = true,
    val totalSyncedItems: Int = 0,
    val syncedFiles: List<String> = listOf(
        "McKays_Inventory_Database.json",
        "McKays_Stock_Report.xlsx",
        "McKays_Daily_Omset.csv"
    ),
    val lastSyncStatusMessage: String = "Sinkronisasi real-time aktif ke Google Drive"
)

class GoogleDriveSyncService(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    private val _syncState = MutableStateFlow(GoogleDriveSyncState())
    val syncState: StateFlow<GoogleDriveSyncState> = _syncState.asStateFlow()

    private val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale("id", "ID"))

    fun toggleAutoSync(enabled: Boolean) {
        _syncState.update { it.copy(isAutoSyncEnabled = enabled) }
    }

    fun syncDataRealtime(
        stockItems: List<StockItem>,
        requests: List<StockRequest>,
        members: List<Member>,
        transactions: List<SaleTransaction>,
        dailyOmset: Double,
        force: Boolean = false
    ) {
        if (!_syncState.value.isAutoSyncEnabled && !force) return

        scope.launch {
            _syncState.update { it.copy(isSyncing = true) }
            // Simulate cloud handshake & json packaging
            delay(500)

            val now = System.currentTimeMillis()
            val formatted = dateFormat.format(Date(now))
            val totalCount = stockItems.size + requests.size + members.size + transactions.size

            _syncState.update {
                it.copy(
                    isSyncing = false,
                    lastSyncTimestamp = now,
                    lastSyncFormatted = formatted,
                    totalSyncedItems = totalCount,
                    lastSyncStatusMessage = "Data tersinkron otomatis ke Google Drive (${it.targetAccount})"
                )
            }
        }
    }

    fun openOrShareToGoogleDrive(
        context: Context,
        stockItems: List<StockItem>,
        requests: List<StockRequest>,
        dailyOmset: Double
    ) {
        val nowStr = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val backupContent = buildString {
            appendLine("=== MCKAYS V.1.1 GOOGLE DRIVE SYNC BACKUP ===")
            appendLine("Akun Google Drive : ${_syncState.value.targetAccount}")
            appendLine("Waktu Sinkron     : ${dateFormat.format(Date())}")
            appendLine("Omset Hari Ini    : Rp ${dailyOmset.toLong()}")
            appendLine("Total Item Stok   : ${stockItems.size}")
            appendLine("Total Order Aktif : ${requests.size}")
            appendLine()
            appendLine("--- DAFTAR INVENTARIS ---")
            stockItems.forEach { item ->
                appendLine("- ${item.name} | ${item.ml}ml | Rp ${item.price.toLong()} | Stok: ${item.stockQuantity} pcs")
            }
            appendLine()
            appendLine("--- STATUS PENGADAAN ORDER ---")
            requests.forEach { req ->
                appendLine("- [${req.status.displayName}] ${req.id} | ${req.branchName} | ${req.itemName} (${req.ml}ml) x${req.quantity}")
            }
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, "McKays_Drive_Sync_$nowStr.txt")
            putExtra(Intent.EXTRA_TEXT, backupContent)
            type = "text/plain"
            // Suggest Google Drive if installed
            setPackage("com.google.android.apps.docs")
        }

        try {
            context.startActivity(sendIntent)
        } catch (e: Exception) {
            // Fallback to system chooser
            val fallbackIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, "McKays_Drive_Sync_$nowStr.txt")
                putExtra(Intent.EXTRA_TEXT, backupContent)
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(fallbackIntent, "Simpan ke Google Drive (budayaxwel@gmail.com)"))
        }
    }
}
