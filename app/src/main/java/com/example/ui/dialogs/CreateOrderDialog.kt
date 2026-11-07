package com.example.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ui.theme.RoyalBlue900

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateOrderDialog(
    initialBranchName: String,
    onDismiss: () -> Unit,
    onSubmit: (branchName: String, itemName: String, ml: Int, quantity: Int, notes: String) -> Unit
) {
    var branchName by remember { mutableStateOf(initialBranchName) }
    var itemName by remember { mutableStateOf("") }
    var selectedMl by remember { mutableIntStateOf(50) }
    var quantityStr by remember { mutableStateOf("10") }
    var notes by remember { mutableStateOf("") }

    val bottleOptions = listOf(10, 15, 20, 30, 50, 100)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Buat Request Order Stok",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "Aturan Pengadaan: Setiap pesanan tidak langsung masuk stok cabang. Permintaan akan masuk ke Admin Pusat untuk ditinjau dan disetujui secara manual.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                OutlinedTextField(
                    value = branchName,
                    onValueChange = { branchName = it },
                    label = { Text("Nama Toko Cabang") },
                    leadingIcon = { Icon(Icons.Default.Store, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Nama Item Parfum") },
                    placeholder = { Text("Contoh: DHL Blue Desiro") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_order_item")
                )

                Text(
                    text = "Varian Ukuran Botol:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    bottleOptions.forEach { ml ->
                        val isSelected = selectedMl == ml
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedMl = ml },
                            label = { Text("${ml}ml", fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            leadingIcon = if (isSelected) {
                                { Icon(Icons.Default.LocalDrink, contentDescription = null) }
                            } else null,
                            shape = RoundedCornerShape(14.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1E3A8A),
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("order_ml_${ml}ml")
                        )
                    }
                }

                OutlinedTextField(
                    value = quantityStr,
                    onValueChange = { quantityStr = it },
                    label = { Text("Jumlah Botol (Quantity)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_order_qty")
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan Pengadaan (Opsional)") },
                    placeholder = { Text("Misal: Persiapan stok promo") },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalItem = itemName.ifBlank { "DHL Blue Desiro" }
                    val finalQty = quantityStr.toIntOrNull() ?: 10
                    onSubmit(branchName, finalItem, selectedMl, finalQty, notes)
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue900),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("submit_order_button")
            ) {
                Text("Kirim ke Admin Pusat", fontWeight = FontWeight.Bold)
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
