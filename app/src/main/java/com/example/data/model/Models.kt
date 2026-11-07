package com.example.data.model

enum class UserRole {
    ADMIN,
    STAFF
}

enum class BottleSize(val label: String, val ml: Int) {
    SIZE_10ML("10ml", 10),
    SIZE_15ML("15ml", 15),
    SIZE_20ML("20ml", 20),
    SIZE_30ML("30ml", 30),
    SIZE_50ML("50ml", 50),
    SIZE_100ML("100ml", 100);

    companion object {
        fun fromMl(ml: Int): BottleSize = entries.find { it.ml == ml } ?: SIZE_30ML
        fun fromLabel(label: String): BottleSize = entries.find { it.label == label } ?: SIZE_30ML
    }
}

enum class StockRequestStatus(val displayName: String) {
    PENDING_ADMIN("Menunggu Persetujuan Admin"),
    APPROVED_ADMIN("Disetujui Admin"),
    RECEIVED_BRANCH("Selesai (Stok Diterima Cabang)"),
    REJECTED("Ditolak")
}

data class UserAccount(
    val email: String,
    val name: String,
    val role: UserRole,
    val branchName: String
)

data class StockItem(
    val id: String,
    val name: String,
    val ml: Int,
    val price: Double,
    val stockQuantity: Int,
    val category: String = "Eau de Parfum",
    val scentNotes: String = "Citrus, Woody, Amber"
)

data class StockRequest(
    val id: String,
    val branchName: String,
    val itemName: String,
    val ml: Int,
    val quantity: Int,
    val status: StockRequestStatus,
    val requestedAt: String,
    val approvedAt: String? = null,
    val notes: String = ""
)

data class Member(
    val id: String,
    val name: String,
    val phone: String,
    val domisili: String,
    val points: Int,
    val purchases: List<String> = emptyList(),
    val registeredAt: String = "2026-01-15"
)

data class CartItem(
    val stockItem: StockItem,
    val quantity: Int
) {
    val subtotal: Double get() = stockItem.price * quantity
}

data class SaleItem(
    val stockItemId: String,
    val itemName: String,
    val ml: Int,
    val unitPrice: Double,
    val quantity: Int,
    val subtotal: Double
)

data class SaleTransaction(
    val id: String,
    val timestamp: Long,
    val dateIso: String,
    val branchName: String,
    val items: List<SaleItem>,
    val totalAmount: Double,
    val paymentMethod: String,
    val memberId: String? = null,
    val memberName: String? = null,
    val pointsEarned: Int = 0
)

data class BranchStaff(
    val id: String,
    val title: String,
    val type: String, // "Cabang" or "Staff"
    val email: String,
    val detail: String
)
