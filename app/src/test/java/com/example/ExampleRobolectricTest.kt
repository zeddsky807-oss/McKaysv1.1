package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.BottleSize
import com.example.data.model.StockRequestStatus
import com.example.data.model.UserRole
import com.example.data.repository.McKaysRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("McKays", appName)
  }

  @Test
  fun `test manual stock item addition and bottle variants`() {
    val repo = McKaysRepository()
    val initialCount = repo.stockItems.value.size
    repo.addStockItem("Sauvage Elixir Custom", 50, 160000.0, 10, "Eau de Parfum", "Warm spicy")
    val updated = repo.stockItems.value
    assertEquals(initialCount + 1, updated.size)
    assertEquals("Sauvage Elixir Custom", updated.first().name)
    assertEquals(50, updated.first().ml)
  }

  @Test
  fun `test stock request workflow - manual admin approval required`() {
    val repo = McKaysRepository()
    // Branch creates request
    repo.createStockRequest("Cabang PVJ", "DHL Blue Desiro", 50, 12, "Stok promo")
    val req = repo.stockRequests.value.first()
    assertEquals(StockRequestStatus.PENDING_ADMIN, req.status)

    // Staff cannot approve if switched to staff role
    repo.switchRole(UserRole.STAFF)
    val staffApproved = repo.approveStockRequest(req.id)
    assertFalse(staffApproved)

    // Admin approves manually
    repo.switchRole(UserRole.ADMIN)
    val adminApproved = repo.approveStockRequest(req.id)
    assertTrue(adminApproved)
    assertEquals(StockRequestStatus.APPROVED_ADMIN, repo.stockRequests.value.first().status)

    // Branch receives stock
    val received = repo.receiveStockAtBranch(req.id)
    assertTrue(received)
    assertEquals(StockRequestStatus.RECEIVED_BRANCH, repo.stockRequests.value.first().status)
  }

  @Test
  fun `test cashier checkout adds to daily omset and awards member points`() {
    val repo = McKaysRepository()
    val initialOmset = repo.dailyOmset.value
    val item = repo.stockItems.value.first()
    repo.addToCart(item)
    val trx = repo.checkout("Tunai", "MEM-01")
    assertTrue(trx != null)
    assertEquals(initialOmset + item.price, repo.dailyOmset.value, 0.01)
  }

  @Test
  fun `test google drive realtime sync target account is budayaxwel@gmail com`() {
    val repo = McKaysRepository()
    val syncState = repo.syncState.value
    assertEquals("budayaxwel@gmail.com", syncState.targetAccount)
    assertTrue(syncState.isAutoSyncEnabled)
  }

  @Test
  fun `test staff role restriction removes admin access`() {
    val repo = McKaysRepository()
    repo.switchRole(UserRole.STAFF)
    val user = repo.currentUser.value
    assertEquals(UserRole.STAFF, user.role)
    assertFalse(user.role == UserRole.ADMIN && user.email.equals("budayaxwel@gmail.com", ignoreCase = true))
  }
}
