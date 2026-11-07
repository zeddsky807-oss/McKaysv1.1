package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SaleTransaction
import com.example.data.model.StockItem
import com.example.ui.McKaysViewModel
import com.example.ui.components.GlassmorphicCard
import com.example.ui.dialogs.AddStockItemDialog
import com.example.ui.dialogs.CheckoutDialog
import com.example.ui.dialogs.ReceiptDialog
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Gold500
import com.example.ui.theme.Rose500
import com.example.ui.theme.RoyalBlue900
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SalesTab(
    viewModel: McKaysViewModel
) {
    val stockItems by viewModel.stockItems.collectAsState()
    val cart by viewModel.cart.collectAsState()
    val members by viewModel.members.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var showCartSheet by remember { mutableStateOf(false) }
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var activeReceipt by remember { mutableStateOf<SaleTransaction?>(null) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterMl by remember { mutableIntStateOf(0) } // 0 = all

    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply { maximumFractionDigits = 0 }
    }

    val bottleOptions = listOf(0, 10, 15, 20, 30, 50, 100)

    val filteredItems = stockItems.filter { item ->
        val matchesSearch = item.name.contains(searchQuery, ignoreCase = true) ||
                item.category.contains(searchQuery, ignoreCase = true)
        val matchesMl = selectedFilterMl == 0 || item.ml == selectedFilterMl
        matchesSearch && matchesMl
    }

    val totalCartCount = cart.sumOf { it.quantity }
    val totalCartAmount = cart.sumOf { it.subtotal }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Kasir & Tambah Item Manual",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Kelola item stok parfum manual atau lakukan transaksi kasir.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Big Action Button: Tambah Item Stok & Varian Botol Manual
            item {
                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue900),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("add_custom_stock_button")
                ) {
                    Icon(Icons.Default.AddBox, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tambah Item Stok & Varian Botol",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari aroma parfum atau kategori...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(18.dp),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_perfume_input")
                )
            }

            // Bottle Size Filter Chips (10ml, 15ml, 20ml, 30ml, 50ml, 100ml)
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    bottleOptions.forEach { ml ->
                        val isSelected = selectedFilterMl == ml
                        val label = if (ml == 0) "Semua Ukuran" else "${ml}ml"
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilterMl = ml },
                            label = { Text(label, fontSize = 12.sp) },
                            shape = RoundedCornerShape(14.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1E3A8A),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Product List
            items(filteredItems, key = { it.id }) { item ->
                ProductStockCard(
                    item = item,
                    currencyFormat = currencyFormat,
                    onAddToCart = { viewModel.addToCart(item) }
                )
            }

            // Bottom space for floating nav and cart summary
            item {
                Spacer(modifier = Modifier.height(140.dp))
            }
        }

        // Floating Cart Summary Bar (if cart has items)
        if (totalCartCount > 0) {
            Surface(
                shape = RoundedCornerShape(26.dp),
                color = RoyalBlue900,
                shadowElevation = 10.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 95.dp)
                    .fillMaxWidth()
                    .testTag("floating_cart_bar")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White)
                            }
                        }
                        Column {
                            Text(
                                text = "$totalCartCount Botol di Keranjang",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                            Text(
                                text = currencyFormat.format(totalCartAmount),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Button(
                        onClick = { showCartSheet = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.testTag("open_cart_sheet_button")
                    ) {
                        Text("Bayar / Kasir", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet: Cart items breakdown
    if (showCartSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCartSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Keranjang Kasir (${totalCartCount} item)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { viewModel.clearCart() }) {
                        Text("Kosongkan", color = Rose500)
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cart, key = { it.stockItem.id }) { cartItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cartItem.stockItem.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${cartItem.stockItem.ml}ml • ${currencyFormat.format(cartItem.stockItem.price)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Subtotal: ${currencyFormat.format(cartItem.subtotal)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { viewModel.updateCartQuantity(cartItem.stockItem.id, cartItem.quantity - 1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        if (cartItem.quantity == 1) Icons.Default.Delete else Icons.Default.Remove,
                                        contentDescription = "Kurang",
                                        tint = if (cartItem.quantity == 1) Rose500 else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Text(
                                    text = "${cartItem.quantity}",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                IconButton(
                                    onClick = { viewModel.updateCartQuantity(cartItem.stockItem.id, cartItem.quantity + 1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Tambah")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total Tagihan:", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = currencyFormat.format(totalCartAmount),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        showCartSheet = false
                        showCheckoutDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue900),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("checkout_button")
                ) {
                    Text("Lanjutkan ke Pembayaran", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    // Dialog Tambah Item Manual
    if (showAddDialog) {
        AddStockItemDialog(
            onDismiss = { showAddDialog = false },
            onSave = { name, ml, price, stock, category, notes ->
                viewModel.addStockItem(name, ml, price, stock, category, notes)
                showAddDialog = false
            }
        )
    }

    // Dialog Checkout Kasir
    if (showCheckoutDialog) {
        CheckoutDialog(
            cartItems = cart,
            members = members,
            onDismiss = { showCheckoutDialog = false },
            onConfirm = { paymentMethod, memberId ->
                val result = viewModel.checkout(paymentMethod, memberId)
                showCheckoutDialog = false
                if (result != null) {
                    activeReceipt = result
                }
            }
        )
    }

    // Dialog Cetak & Bagikan Nota Resmi
    activeReceipt?.let { trx ->
        ReceiptDialog(
            transaction = trx,
            onDismiss = { activeReceipt = null }
        )
    }
}

@Composable
private fun ProductStockCard(
    item: StockItem,
    currencyFormat: NumberFormat,
    onAddToCart: () -> Unit
) {
    GlassmorphicCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF1E3A8A).copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "${item.ml}ml",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${item.category} • Notes: ${item.scentNotes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = currencyFormat.format(item.price),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Emerald500
                    )
                    Text(
                        text = "Stok: ${item.stockQuantity} pcs",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (item.stockQuantity <= 10) Rose500 else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (item.stockQuantity <= 10) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = onAddToCart,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
                shape = RoundedCornerShape(14.dp),
                enabled = item.stockQuantity > 0,
                modifier = Modifier.testTag("add_to_cart_${item.id}")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Kasir", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
