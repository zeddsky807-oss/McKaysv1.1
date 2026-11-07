package com.example.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.model.BottleSize
import com.example.ui.theme.RoyalBlue900

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddStockItemDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, ml: Int, price: Double, stock: Int, category: String, notes: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedMl by remember { mutableIntStateOf(30) }
    var priceStr by remember { mutableStateOf("90000") }
    var stockStr by remember { mutableStateOf("15") }
    var category by remember { mutableStateOf("Eau de Parfum") }
    var scentNotes by remember { mutableStateOf("Citrus, Amber, Woody") }

    val bottleOptions = listOf(10, 15, 20, 30, 50, 100)
    val categories = listOf("Eau de Parfum", "Extrait de Parfum", "Oil Blend", "Pure Perfume")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Tambah Item Stok & Varian Botol",
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
                Text(
                    text = "Input item stok baru secara manual sesuai kebutuhan cabang.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Produk / Item Stok") },
                    placeholder = { Text("Contoh: DHL Blue Desiro") },
                    leadingIcon = { Icon(Icons.Default.ShoppingBag, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_stock_name")
                )

                Text(
                    text = "Pilihan Ukuran Botol:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
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
                            modifier = Modifier.testTag("bottle_chip_${ml}ml")
                        )
                    }
                }

                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it },
                    label = { Text("Harga Jual (Rp)") },
                    prefix = { Text("Rp ") },
                    leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_stock_price")
                )

                OutlinedTextField(
                    value = stockStr,
                    onValueChange = { stockStr = it },
                    label = { Text("Jumlah Stok Fisik Awal") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_stock_qty")
                )

                Text(
                    text = "Kategori Konsentrasi:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = scentNotes,
                    onValueChange = { scentNotes = it },
                    label = { Text("Aroma / Scent Profile") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalName = name.ifBlank { "Parfum Custom" }
                    val finalPrice = priceStr.toDoubleOrNull() ?: 0.0
                    val finalStock = stockStr.toIntOrNull() ?: 0
                    onSave(finalName, selectedMl, finalPrice, finalStock, category, scentNotes)
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue900),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("save_stock_button")
            ) {
                Text("Simpan Stok & Jual", fontWeight = FontWeight.Bold)
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
