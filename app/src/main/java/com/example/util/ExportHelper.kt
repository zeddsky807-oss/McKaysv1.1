package com.example.util

import android.content.Context
import android.content.Intent
import com.example.data.model.Member
import com.example.data.model.SaleTransaction
import com.example.data.model.StockItem
import com.example.data.model.StockRequest
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExportHelper {

    private val idrFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
        maximumFractionDigits = 0
    }

    fun exportToExcel(
        context: Context,
        stockList: List<StockItem>,
        requests: List<StockRequest>,
        members: List<Member>,
        transactions: List<SaleTransaction>,
        dailyOmset: Double
    ) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        val nowStr = dateFormat.format(Date())

        val content = buildString {
            appendLine("=== LAPORAN INVENTARIS & PENJUALAN MCKAYS V.1.1 (EXCEL FORMAT) ===")
            appendLine("Diekspor pada: $nowStr")
            appendLine("Omset Terkini: ${idrFormat.format(dailyOmset)}")
            appendLine()

            appendLine("--- DAFTAR STOK PARFUM ---")
            appendLine("No\tNama Produk\tUkuran (ml)\tHarga (IDR)\tStok Tersedia\tKategori")
            stockList.forEachIndexed { idx, item ->
                appendLine("${idx + 1}\t${item.name}\t${item.ml}ml\t${item.price.toLong()}\t${item.stockQuantity}\t${item.category}")
            }
            appendLine()

            appendLine("--- PENGADAAN REQUEST ORDER STOK CABANG ---")
            appendLine("ID Order\tCabang\tItem\tUkuran\tJumlah\tStatus Persetujuan\tWaktu")
            requests.forEach { req ->
                appendLine("${req.id}\t${req.branchName}\t${req.itemName}\t${req.ml}ml\t${req.quantity}\t${req.status.displayName}\t${req.requestedAt}")
            }
            appendLine()

            appendLine("--- DATA MEMBER & LOYALTY ---")
            appendLine("Nama Member\tNomor Telepon\tDomisili\tTotal Poin\tRiwayat Pembelian")
            members.forEach { m ->
                appendLine("${m.name}\t${m.phone}\t${m.domisili}\t${m.points}\t${m.purchases.joinToString("; ")}")
            }
            appendLine()

            appendLine("--- RIWAYAT TRANSAKSI TERAKHIR ---")
            appendLine("ID Transaksi\tTanggal\tCabang\tPembeli\tMetode\tTotal (IDR)")
            transactions.forEach { t ->
                appendLine("${t.id}\t${t.dateIso}\t${t.branchName}\t${t.memberName ?: "Umum"}\t${t.paymentMethod}\t${t.totalAmount.toLong()}")
            }
        }

        shareTextFile(context, "McKays_Report_${System.currentTimeMillis()}.xls", content, "application/vnd.ms-excel", "Ekspor Laporan Excel McKays V.1.1")
    }

    fun exportToCsv(
        context: Context,
        stockList: List<StockItem>,
        requests: List<StockRequest>,
        dailyOmset: Double
    ) {
        val content = buildString {
            appendLine("\"ID\",\"Nama Produk\",\"Varian ml\",\"Harga\",\"Stok Fisik\",\"Kategori\"")
            stockList.forEach { s ->
                appendLine("\"${s.id}\",\"${s.name.replace("\"", "\"\"")}\",\"${s.ml}\",\"${s.price.toLong()}\",\"${s.stockQuantity}\",\"${s.category}\"")
            }
            appendLine()
            appendLine("\"ID Request\",\"Cabang\",\"Item\",\"Varian ml\",\"Qty\",\"Status Pengadaan\"")
            requests.forEach { r ->
                appendLine("\"${r.id}\",\"${r.branchName}\",\"${r.itemName}\",\"${r.ml}\",\"${r.quantity}\",\"${r.status.displayName}\"")
            }
        }

        shareTextFile(context, "McKays_Stok_Export.csv", content, "text/csv", "Ekspor Data CSV McKays V.1.1")
    }

    fun exportToPdfSummary(
        context: Context,
        stockList: List<StockItem>,
        requests: List<StockRequest>,
        members: List<Member>,
        dailyOmset: Double
    ) {
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale("id", "ID"))
        val nowStr = dateFormat.format(Date())

        val content = buildString {
            appendLine("=====================================================")
            appendLine("                 MCKAYS PARFUM V.1.1                 ")
            appendLine("       LAPORAN RESMI ADMINISTRATOR (PDF DOCUMENT)    ")
            appendLine("=====================================================")
            appendLine("Waktu Cetak       : $nowStr")
            appendLine("Status Gudang     : Gudang Stok Pusat (Admin)")
            appendLine("Akun Admin        : budayaxwel@gmail.com")
            appendLine("Omset Hari Ini    : ${idrFormat.format(dailyOmset)}")
            appendLine("Total Varian Stok : ${stockList.size} item")
            appendLine("Total Order Aktif : ${requests.size} request")
            appendLine("Total Pelanggan   : ${members.size} member")
            appendLine("-----------------------------------------------------")
            appendLine("1. DAFTAR INVENTARIS STOK PARFUM:")
            stockList.forEachIndexed { i, s ->
                appendLine("   ${i + 1}. ${s.name} (${s.ml}ml) | Stok: ${s.stockQuantity} pcs | Harga: ${idrFormat.format(s.price)}")
            }
            appendLine("-----------------------------------------------------")
            appendLine("2. STATUS REQUEST ORDER PENGADAAN CABANG:")
            requests.forEachIndexed { i, r ->
                appendLine("   ${i + 1}. [${r.status.displayName}]")
                appendLine("      Order ID : ${r.id} | ${r.branchName}")
                appendLine("      Item     : ${r.itemName} (${r.ml}ml) x ${r.quantity} pcs")
            }
            appendLine("-----------------------------------------------------")
            appendLine("3. DATA MEMBER LOYALITAS TERDAFTAR:")
            members.forEachIndexed { i, m ->
                appendLine("   ${i + 1}. ${m.name} (${m.phone}) - Domisili: ${m.domisili}")
                appendLine("      Poin Reward: ${m.points} Poin | Riwayat: ${m.purchases.joinToString(", ")}")
            }
            appendLine("=====================================================")
            appendLine("     Dokumen Digital Resmi McKays Inventory V.1.1    ")
            appendLine("=====================================================")
        }

        shareTextFile(context, "McKays_Dokumen_Resmi.pdf", content, "text/plain", "Bagikan Dokumen Laporan McKays")
    }

    private fun shareTextFile(context: Context, filename: String, textContent: String, mimeType: String, chooserTitle: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, chooserTitle)
            putExtra(Intent.EXTRA_TITLE, filename)
            putExtra(Intent.EXTRA_TEXT, textContent)
            type = mimeType
        }
        val chooser = Intent.createChooser(sendIntent, chooserTitle).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }
}
