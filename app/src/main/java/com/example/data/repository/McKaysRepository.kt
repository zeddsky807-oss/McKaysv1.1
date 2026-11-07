package com.example.data.repository

import com.example.data.model.BranchStaff
import com.example.data.model.CartItem
import com.example.data.model.Member
import com.example.data.model.SaleItem
import com.example.data.model.SaleTransaction
import com.example.data.model.StockItem
import com.example.data.model.StockRequest
import com.example.data.model.StockRequestStatus
import com.example.data.model.UserAccount
import com.example.data.model.UserRole
import com.example.data.sync.GoogleDriveSyncService
import com.example.data.sync.GoogleDriveSyncState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class McKaysRepository {

    val syncService = GoogleDriveSyncService()
    val syncState: StateFlow<GoogleDriveSyncState> = syncService.syncState

    // Current logged in user (Admin by default)
    private val _currentUser = MutableStateFlow(
        UserAccount(
            email = "budayaxwel@gmail.com",
            name = "Admin Pusat",
            role = UserRole.ADMIN,
            branchName = "Gudang Stok Pusat"
        )
    )
    val currentUser: StateFlow<UserAccount> = _currentUser.asStateFlow()

    // Daily Omset & Selected Calendar Date
    private val _dailyOmset = MutableStateFlow(450000.0)
    val dailyOmset: StateFlow<Double> = _dailyOmset.asStateFlow()

    private val _selectedDateMillis = MutableStateFlow(System.currentTimeMillis())
    val selectedDateMillis: StateFlow<Long> = _selectedDateMillis.asStateFlow()

    // Stock Items Catalog
    private val _stockItems = MutableStateFlow<List<StockItem>>(
        listOf(
            StockItem("1", "DHL Blue Desiro", 30, 90000.0, 24, "Eau de Parfum", "Aquatic, Bergamot, Amberwood"),
            StockItem("2", "DHL Blue Desiro", 50, 145000.0, 18, "Eau de Parfum", "Aquatic, Bergamot, Amberwood"),
            StockItem("3", "CH Manhattan", 30, 95000.0, 16, "Extrait de Parfum", "Leather, Tobacco, Warm Spicy"),
            StockItem("4", "CH Manhattan", 50, 150000.0, 12, "Extrait de Parfum", "Leather, Tobacco, Warm Spicy"),
            StockItem("5", "Baccarat Rouge 540", 50, 175000.0, 10, "Extrait de Parfum", "Jasmine, Saffron, Cedarwood"),
            StockItem("6", "Black Opium Floral", 30, 95000.0, 20, "Eau de Parfum", "Coffee, White Flowers, Vanilla"),
            StockItem("7", "Savage Elixir", 100, 220000.0, 8, "Eau de Parfum", "Nutmeg, Cinnamon, Cardamom"),
            StockItem("8", "Vanilla Bourbon", 15, 55000.0, 30, "Oil Blend", "Madagascar Vanilla, Caramel"),
            StockItem("9", "Silver Mountain", 20, 70000.0, 25, "Eau de Parfum", "Green Tea, Blackcurrant"),
            StockItem("10", "Rose de Grasse", 10, 45000.0, 40, "Pure Perfume", "May Rose, Turkish Rose")
        )
    )
    val stockItems: StateFlow<List<StockItem>> = _stockItems.asStateFlow()

    // Stock Request Orders (Branches to Admin)
    private val _stockRequests = MutableStateFlow<List<StockRequest>>(
        listOf(
            StockRequest(
                id = "REQ-01",
                branchName = "Cabang Paris Van Java (Bandung)",
                itemName = "DHL Blue Desiro",
                ml = 50,
                quantity = 15,
                status = StockRequestStatus.PENDING_ADMIN,
                requestedAt = "06 Nov 2026, 09:30",
                notes = "Stok menipis menjelang akhir pekan"
            ),
            StockRequest(
                id = "REQ-02",
                branchName = "Cabang Tunjungan Plaza (Surabaya)",
                itemName = "CH Manhattan",
                ml = 30,
                quantity = 10,
                status = StockRequestStatus.APPROVED_ADMIN,
                requestedAt = "05 Nov 2026, 14:15",
                approvedAt = "05 Nov 2026, 16:00",
                notes = "Persetujuan selesai, siap diterima cabang"
            )
        )
    )
    val stockRequests: StateFlow<List<StockRequest>> = _stockRequests.asStateFlow()

    // Members Loyalty & History
    private val _members = MutableStateFlow<List<Member>>(
        listOf(
            Member(
                id = "MEM-01",
                name = "Siti Rahma",
                phone = "081234567890",
                domisili = "Bandung",
                points = 450,
                purchases = listOf("DHL Blue Desiro (30ml)", "CH Manhattan (50ml)"),
                registeredAt = "15 Jan 2026"
            ),
            Member(
                id = "MEM-02",
                name = "Budi Santoso",
                phone = "085712349988",
                domisili = "Jakarta",
                points = 320,
                purchases = listOf("Savage Elixir (100ml)"),
                registeredAt = "02 Feb 2026"
            ),
            Member(
                id = "MEM-03",
                name = "Amanda Putri",
                phone = "082198765432",
                domisili = "Surabaya",
                points = 600,
                purchases = listOf("Vanilla Bourbon (15ml)", "Baccarat Rouge 540 (50ml)"),
                registeredAt = "10 Mar 2026"
            )
        )
    )
    val members: StateFlow<List<Member>> = _members.asStateFlow()

    // Branches & Staff accounts managed by Admin
    private val _branchesAndStaff = MutableStateFlow<List<BranchStaff>>(
        listOf(
            BranchStaff("B1", "Cabang Paris Van Java", "Cabang", "pvj@mckays.com", "Mall PVJ Bandung - Ground Floor"),
            BranchStaff("B2", "Cabang Tunjungan Plaza", "Cabang", "tp@mckays.com", "TP 6 Lantai 2 Surabaya"),
            BranchStaff("B3", "Cabang Senayan City", "Cabang", "sency@mckays.com", "Senayan City Jakarta Pusat"),
            BranchStaff("S1", "Staff Kasir: Budi", "Staff", "budi.kasir@mckays.com", "Penempatan Cabang PVJ"),
            BranchStaff("S2", "Staff Kasir: Rian", "Staff", "rian.kasir@mckays.com", "Penempatan Cabang Tunjungan Plaza")
        )
    )
    val branchesAndStaff: StateFlow<List<BranchStaff>> = _branchesAndStaff.asStateFlow()

    // Cart in Cashier
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    // Sales Transactions
    private val _transactions = MutableStateFlow<List<SaleTransaction>>(emptyList())
    val transactions: StateFlow<List<SaleTransaction>> = _transactions.asStateFlow()

    // ---------------- Auth & Role Methods ----------------
    fun login(email: String, name: String? = null, role: UserRole? = null) {
        val assignedRole = role ?: if (email.trim().equals("budayaxwel@gmail.com", ignoreCase = true)) {
            UserRole.ADMIN
        } else {
            UserRole.STAFF
        }
        val assignedName = name ?: if (assignedRole == UserRole.ADMIN) "Admin Pusat" else "Staff Cabang PVJ"
        val assignedBranch = if (assignedRole == UserRole.ADMIN) "Gudang Stok Pusat (Admin)" else "Cabang Paris Van Java"

        _currentUser.value = UserAccount(
            email = email.trim(),
            name = assignedName,
            role = assignedRole,
            branchName = assignedBranch
        )
    }

    fun switchRole(toRole: UserRole) {
        if (toRole == UserRole.ADMIN) {
            _currentUser.value = UserAccount(
                email = "budayaxwel@gmail.com",
                name = "Admin Pusat",
                role = UserRole.ADMIN,
                branchName = "Gudang Stok Pusat"
            )
        } else {
            _currentUser.value = UserAccount(
                email = "kasir.pvj@mckays.com",
                name = "Budi (Staff Kasir)",
                role = UserRole.STAFF,
                branchName = "Cabang Paris Van Java"
            )
        }
    }

    // ---------------- Omset & Calendar ----------------
    fun updateDailyOmset(amount: Double) {
        _dailyOmset.value = amount
        syncService.syncDataRealtime(_stockItems.value, _stockRequests.value, _members.value, _transactions.value, amount)
    }

    fun setSelectedDate(millis: Long) {
        _selectedDateMillis.value = millis
    }

    // ---------------- Stock Management ----------------
    fun addStockItem(name: String, ml: Int, price: Double, initialStock: Int, category: String = "Eau de Parfum", notes: String = "Premium Perfume") {
        val newItem = StockItem(
            id = UUID.randomUUID().toString().take(8),
            name = name.ifBlank { "Parfum Custom" },
            ml = ml,
            price = price,
            stockQuantity = initialStock.coerceAtLeast(0),
            category = category,
            scentNotes = notes
        )
        _stockItems.update { current -> listOf(newItem) + current }
        syncService.syncDataRealtime(_stockItems.value, _stockRequests.value, _members.value, _transactions.value, _dailyOmset.value)
    }

    // ---------------- Request Order Workflow ----------------
    fun createStockRequest(branchName: String, itemName: String, ml: Int, quantity: Int, notes: String) {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        val newReq = StockRequest(
            id = "REQ-" + (System.currentTimeMillis() % 10000),
            branchName = branchName,
            itemName = itemName,
            ml = ml,
            quantity = quantity,
            status = StockRequestStatus.PENDING_ADMIN,
            requestedAt = sdf.format(Date()),
            notes = notes
        )
        _stockRequests.update { listOf(newReq) + it }
        syncService.syncDataRealtime(_stockItems.value, _stockRequests.value, _members.value, _transactions.value, _dailyOmset.value)
    }

    fun approveStockRequest(requestId: String): Boolean {
        if (_currentUser.value.role != UserRole.ADMIN) return false
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        _stockRequests.update { list ->
            list.map { req ->
                if (req.id == requestId) {
                    req.copy(status = StockRequestStatus.APPROVED_ADMIN, approvedAt = sdf.format(Date()))
                } else req
            }
        }
        syncService.syncDataRealtime(_stockItems.value, _stockRequests.value, _members.value, _transactions.value, _dailyOmset.value)
        return true
    }

    fun rejectStockRequest(requestId: String): Boolean {
        if (_currentUser.value.role != UserRole.ADMIN) return false
        _stockRequests.update { list ->
            list.map { req ->
                if (req.id == requestId) {
                    req.copy(status = StockRequestStatus.REJECTED)
                } else req
            }
        }
        syncService.syncDataRealtime(_stockItems.value, _stockRequests.value, _members.value, _transactions.value, _dailyOmset.value)
        return true
    }

    fun receiveStockAtBranch(requestId: String): Boolean {
        var foundReq: StockRequest? = null
        _stockRequests.update { list ->
            list.map { req ->
                if (req.id == requestId && req.status == StockRequestStatus.APPROVED_ADMIN) {
                    foundReq = req
                    req.copy(status = StockRequestStatus.RECEIVED_BRANCH)
                } else req
            }
        }

        // Add requested quantity to stock inventory
        foundReq?.let { req ->
            val match = _stockItems.value.find { it.name.equals(req.itemName, ignoreCase = true) && it.ml == req.ml }
            if (match != null) {
                _stockItems.update { items ->
                    items.map { item ->
                        if (item.id == match.id) {
                            item.copy(stockQuantity = item.stockQuantity + req.quantity)
                        } else item
                    }
                }
            } else {
                // Insert as new inventory entry
                addStockItem(req.itemName, req.ml, 120000.0, req.quantity)
            }
            syncService.syncDataRealtime(_stockItems.value, _stockRequests.value, _members.value, _transactions.value, _dailyOmset.value)
            return true
        }
        return false
    }

    // ---------------- Branch & Staff Management (Admin Only) ----------------
    fun deleteBranchOrStaff(id: String): Boolean {
        if (_currentUser.value.role != UserRole.ADMIN) return false
        _branchesAndStaff.update { current ->
            current.filterNot { it.id == id }
        }
        return true
    }

    fun addBranchOrStaff(title: String, type: String, email: String, detail: String): Boolean {
        if (_currentUser.value.role != UserRole.ADMIN) return false
        val newEntry = BranchStaff(
            id = "BS-" + UUID.randomUUID().toString().take(6),
            title = title,
            type = type,
            email = email,
            detail = detail
        )
        _branchesAndStaff.update { it + newEntry }
        return true
    }

    // ---------------- Members Management ----------------
    fun addMember(name: String, phone: String, domisili: String) {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val newMember = Member(
            id = "MEM-" + (System.currentTimeMillis() % 10000),
            name = name,
            phone = phone,
            domisili = domisili,
            points = 0,
            purchases = emptyList(),
            registeredAt = sdf.format(Date())
        )
        _members.update { listOf(newMember) + it }
        syncService.syncDataRealtime(_stockItems.value, _stockRequests.value, _members.value, _transactions.value, _dailyOmset.value)
    }

    // ---------------- POS & Cart ----------------
    fun addToCart(item: StockItem) {
        _cart.update { current ->
            val existing = current.find { it.stockItem.id == item.id }
            if (existing != null) {
                current.map {
                    if (it.stockItem.id == item.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                current + CartItem(stockItem = item, quantity = 1)
            }
        }
    }

    fun updateCartQuantity(stockItemId: String, newQty: Int) {
        if (newQty <= 0) {
            removeFromCart(stockItemId)
        } else {
            _cart.update { current ->
                current.map {
                    if (it.stockItem.id == stockItemId) it.copy(quantity = newQty) else it
                }
            }
        }
    }

    fun removeFromCart(stockItemId: String) {
        _cart.update { current -> current.filterNot { it.stockItem.id == stockItemId } }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    fun checkout(paymentMethod: String, memberId: String?): SaleTransaction? {
        val currentCart = _cart.value
        if (currentCart.isEmpty()) return null

        val total = currentCart.sumOf { it.subtotal }
        val saleItems = currentCart.map {
            SaleItem(
                stockItemId = it.stockItem.id,
                itemName = it.stockItem.name,
                ml = it.stockItem.ml,
                unitPrice = it.stockItem.price,
                quantity = it.quantity,
                subtotal = it.subtotal
            )
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("id", "ID"))
        val now = Date()
        val points = (total / 10000).toInt()

        val member = _members.value.find { it.id == memberId }

        val transaction = SaleTransaction(
            id = "TRX-" + (System.currentTimeMillis() % 100000),
            timestamp = now.time,
            dateIso = sdf.format(now),
            branchName = _currentUser.value.branchName,
            items = saleItems,
            totalAmount = total,
            paymentMethod = paymentMethod,
            memberId = member?.id,
            memberName = member?.name,
            pointsEarned = points
        )

        // Deduct inventory
        _stockItems.update { items ->
            items.map { stock ->
                val purchased = currentCart.find { it.stockItem.id == stock.id }
                if (purchased != null) {
                    stock.copy(stockQuantity = (stock.stockQuantity - purchased.quantity).coerceAtLeast(0))
                } else stock
            }
        }

        // Add to daily omset real-time
        _dailyOmset.update { it + total }

        // Update member points and purchase history if applicable
        if (member != null) {
            val purchasedNames = currentCart.map { "${it.stockItem.name} (${it.stockItem.ml}ml x${it.quantity})" }
            _members.update { list ->
                list.map { m ->
                    if (m.id == member.id) {
                        m.copy(
                            points = m.points + points,
                            purchases = m.purchases + purchasedNames
                        )
                    } else m
                }
            }
        }

        _transactions.update { listOf(transaction) + it }
        _cart.value = emptyList()
        syncService.syncDataRealtime(_stockItems.value, _stockRequests.value, _members.value, _transactions.value, _dailyOmset.value)
        return transaction
    }
}
