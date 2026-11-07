package com.example.ui.dialogs

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItem
import com.example.data.model.Member
import com.example.data.model.SaleTransaction
import com.example.ui.theme.Emerald500
import com.example.ui.theme.RoyalBlue900
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutDialog(
    cartItems: List<CartItem>,
    members: List<Member>,
    onDismiss: () -> Unit,
    onConfirm: (paymentMethod: String, memberId: String?) -> Unit
) {
    var selectedPayment by remember { mutableStateOf("Tunai") }
    var expandedMemberDropdown by remember { mutableStateOf(false) }
    var selectedMember by remember { mutableStateOf<Member?>(null) }

    val idrFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply { maximumFractionDigits = 0 }
    }
    val totalAmount = cartItems.sumOf { it.subtotal }
    val pointsEarned = (totalAmount / 10000).toInt()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Pembayaran & Checkout Kasir",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Cart summary box
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Ringkasan Belanja (${cartItems.sumOf { it.quantity }} item):",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        cartItems.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${item.stockItem.name} (${item.stockItem.ml}ml) x${item.quantity}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = idrFormat.format(item.subtotal),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Pembayaran",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = idrFormat.format(totalAmount),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Member Selection Dropdown
                Text(
                    text = "Pilih Pelanggan / Member (Poin Reward):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                ExposedDropdownMenuBox(
                    expanded = expandedMemberDropdown,
                    onExpandedChange = { expandedMemberDropdown = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedMember?.let { "${it.name} (${it.phone})" } ?: "Non-Member (Pelanggan Umum)",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMemberDropdown) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedMemberDropdown,
                        onDismissRequest = { expandedMemberDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Non-Member (Pelanggan Umum)") },
                            onClick = {
                                selectedMember = null
                                expandedMemberDropdown = false
                            }
                        )
                        members.forEach { m ->
                            DropdownMenuItem(
                                text = { Text("${m.name} (${m.phone}) • ${m.points} Poin") },
                                onClick = {
                                    selectedMember = m
                                    expandedMemberDropdown = false
                                }
                            )
                        }
                    }
                }

                if (selectedMember != null) {
                    Text(
                        text = "Member akan mendapatkan +$pointsEarned poin dari transaksi ini!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Emerald500,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Payment Method
                Text(
                    text = "Metode Pembayaran:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "Tunai" to Icons.Default.Payments,
                        "QRIS" to Icons.Default.QrCode,
                        "Transfer" to Icons.Default.CreditCard
                    ).forEach { (method, icon) ->
                        val isSelected = selectedPayment == method
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedPayment = method },
                            label = { Text(method) },
                            leadingIcon = { Icon(icon, contentDescription = null) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(selectedPayment, selectedMember?.id)
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue900),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("confirm_checkout_button")
            ) {
                Text("Konfirmasi & Cetak Nota", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        },
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
fun ReceiptDialog(
    transaction: SaleTransaction,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val idrFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply { maximumFractionDigits = 0 }
    }

    val receiptText = buildString {
        appendLine("========================================")
        appendLine("             MCKAYS V.1.1               ")
        appendLine("     NOTA PENJUALAN PARFUM RESMI        ")
        appendLine("========================================")
        appendLine("No Transaksi : ${transaction.id}")
        appendLine("Waktu        : ${transaction.dateIso}")
        appendLine("Cabang       : ${transaction.branchName}")
        appendLine("Pelanggan    : ${transaction.memberName ?: "Pelanggan Umum"}")
        appendLine("Pembayaran   : ${transaction.paymentMethod}")
        appendLine("----------------------------------------")
        transaction.items.forEach { item ->
            appendLine("${item.itemName} (${item.ml}ml)")
            appendLine("  ${item.quantity} x ${idrFormat.format(item.unitPrice)} = ${idrFormat.format(item.subtotal)}")
        }
        appendLine("----------------------------------------")
        appendLine("TOTAL TRANSAKSI : ${idrFormat.format(transaction.totalAmount)}")
        if (transaction.pointsEarned > 0) {
            appendLine("Poin Didapat    : +${transaction.pointsEarned} Poin")
        }
        appendLine("========================================")
        appendLine("   Terima kasih telah berbelanja di    ")
        appendLine("            McKays Parfume              ")
        appendLine("========================================")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald500)
                Text("Transaksi Berhasil!", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Stok berkurang & omset harian telah bertambah secara otomatis.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = receiptText,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue900),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("close_receipt_button")
            ) {
                Text("Selesai")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, receiptText)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Bagikan Nota Pembelian"))
                }
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text("Bagikan Nota")
            }
        },
        shape = RoundedCornerShape(28.dp)
    )
}
