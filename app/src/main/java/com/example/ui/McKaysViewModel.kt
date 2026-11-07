package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.data.model.BottleSize
import com.example.data.model.CartItem
import com.example.data.model.Member
import com.example.data.model.SaleTransaction
import com.example.data.model.StockItem
import com.example.data.model.StockRequest
import com.example.data.model.UserAccount
import com.example.data.model.UserRole
import com.example.data.repository.McKaysRepository
import kotlinx.coroutines.flow.StateFlow

class McKaysViewModel(
    private val repository: McKaysRepository = McKaysRepository()
) : ViewModel() {

    val currentUser: StateFlow<UserAccount> = repository.currentUser
    val dailyOmset: StateFlow<Double> = repository.dailyOmset
    val selectedDateMillis: StateFlow<Long> = repository.selectedDateMillis
    val stockItems: StateFlow<List<StockItem>> = repository.stockItems
    val stockRequests: StateFlow<List<StockRequest>> = repository.stockRequests
    val members: StateFlow<List<Member>> = repository.members
    val branchesAndStaff = repository.branchesAndStaff
    val cart: StateFlow<List<CartItem>> = repository.cart
    val transactions: StateFlow<List<SaleTransaction>> = repository.transactions
    val syncState = repository.syncState

    fun triggerGoogleDriveSync() {
        repository.syncService.syncDataRealtime(
            repository.stockItems.value,
            repository.stockRequests.value,
            repository.members.value,
            repository.transactions.value,
            repository.dailyOmset.value,
            force = true
        )
    }

    fun toggleAutoSync(enabled: Boolean) {
        repository.syncService.toggleAutoSync(enabled)
    }

    fun openOrShareToGoogleDrive(context: android.content.Context) {
        repository.syncService.openOrShareToGoogleDrive(
            context,
            repository.stockItems.value,
            repository.stockRequests.value,
            repository.dailyOmset.value
        )
    }

    // User & Role
    fun login(email: String, role: UserRole? = null) {
        repository.login(email = email, role = role)
    }

    fun switchRole(role: UserRole) {
        repository.switchRole(role)
    }

    // Omset & Gregorian Calendar
    fun updateDailyOmset(amount: Double) {
        repository.updateDailyOmset(amount)
    }

    fun setSelectedDate(millis: Long) {
        repository.setSelectedDate(millis)
    }

    // Manual stock item input with bottle variant options (10ml, 15ml, 20ml, 30ml, 50ml, 100ml)
    fun addStockItem(name: String, ml: Int, price: Double, initialStock: Int, category: String, notes: String) {
        repository.addStockItem(name, ml, price, initialStock, category, notes)
    }

    // Stock Request Workflow (Admin manual review & approval)
    fun createStockRequest(branchName: String, itemName: String, ml: Int, quantity: Int, notes: String) {
        repository.createStockRequest(branchName, itemName, ml, quantity, notes)
    }

    fun approveStockRequest(requestId: String): Boolean {
        return repository.approveStockRequest(requestId)
    }

    fun rejectStockRequest(requestId: String): Boolean {
        return repository.rejectStockRequest(requestId)
    }

    fun receiveStockAtBranch(requestId: String): Boolean {
        return repository.receiveStockAtBranch(requestId)
    }

    // Branch & Staff Management (Admin only)
    fun deleteBranchOrStaff(id: String): Boolean {
        return repository.deleteBranchOrStaff(id)
    }

    fun addBranchOrStaff(title: String, type: String, email: String, detail: String): Boolean {
        return repository.addBranchOrStaff(title, type, email, detail)
    }

    // Member Management
    fun addMember(name: String, phone: String, domisili: String) {
        repository.addMember(name, phone, domisili)
    }

    // POS & Cart
    fun addToCart(item: StockItem) {
        repository.addToCart(item)
    }

    fun updateCartQuantity(stockItemId: String, qty: Int) {
        repository.updateCartQuantity(stockItemId, qty)
    }

    fun removeFromCart(stockItemId: String) {
        repository.removeFromCart(stockItemId)
    }

    fun clearCart() {
        repository.clearCart()
    }

    fun checkout(paymentMethod: String, memberId: String?): SaleTransaction? {
        return repository.checkout(paymentMethod, memberId)
    }
}
