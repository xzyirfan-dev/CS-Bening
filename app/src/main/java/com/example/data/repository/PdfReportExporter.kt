package com.example.data.repository

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.example.data.local.ChecklistTaskEntity
import java.io.File
import java.io.FileOutputStream

object PdfReportExporter {

    fun generatePdfReport(
        context: Context,
        startDateStr: String,
        endDateStr: String,
        monthPeriodStr: String,
        tasks: List<ChecklistTaskEntity>
    ): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paint = Paint()
        val titlePaint = Paint()
        val headerPaint = Paint()

        // Background
        canvas.drawColor(Color.WHITE)

        // Title Header
        titlePaint.color = Color.parseColor("#0F172A")
        titlePaint.textSize = 18f
        titlePaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("DAILY PERFORMANCE TRACKER REPORT", 36f, 50f, titlePaint)

        titlePaint.textSize = 12f
        titlePaint.color = Color.parseColor("#0284C7")
        canvas.drawText("BENINGS GLOW CLINIC GRESIK", 36f, 70f, titlePaint)

        // Subtitle & Period
        paint.color = Color.parseColor("#334155")
        paint.textSize = 10f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Pimpinan: Fitria Nor Istiqomah", 36f, 90f, paint)
        canvas.drawText("Filter Periode: $startDateStr s/d $endDateStr ($monthPeriodStr)", 36f, 105f, paint)

        // Divider
        paint.color = Color.parseColor("#CBD5E1")
        paint.strokeWidth = 1f
        canvas.drawLine(36f, 115f, 559f, 115f, paint)

        // Summary Stats Box
        val completedCount = tasks.count { it.status == "Selesai" }
        val pendingCount = tasks.count { it.status == "Pending" }
        val approvedCount = tasks.count { it.approvalBm == "Approved" }
        val totalCount = tasks.size
        val avgScore = if (tasks.filter { it.score > 0 }.isNotEmpty()) tasks.filter { it.score > 0 }.map { it.score }.average().toInt() else 0

        val boxPaint = Paint()
        boxPaint.color = Color.parseColor("#F1F5F9")
        canvas.drawRect(36f, 125f, 559f, 175f, boxPaint)

        paint.color = Color.parseColor("#0F172A")
        paint.textSize = 10f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("RINGKASAN KINERJA PERIODE", 48f, 142f, paint)

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.textSize = 9f
        canvas.drawText("Total Tugas: $totalCount   |   Selesai: $completedCount   |   Pending: $pendingCount   |   Approved BM: $approvedCount   |   Rata-Rata Skor: $avgScore / 100", 48f, 160f, paint)

        // Table Header
        val startY = 195f
        headerPaint.color = Color.parseColor("#0284C7")
        canvas.drawRect(36f, startY, 559f, startY + 20f, headerPaint)

        paint.color = Color.WHITE
        paint.textSize = 8f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("TANGGAL", 42f, startY + 13f, paint)
        canvas.drawText("AREA / TUGAS", 130f, startY + 13f, paint)
        canvas.drawText("JADWAL", 240f, startY + 13f, paint)
        canvas.drawText("STATUS", 320f, startY + 13f, paint)
        canvas.drawText("APPROVAL BM", 400f, startY + 13f, paint)
        canvas.drawText("SKOR", 480f, startY + 13f, paint)
        canvas.drawText("GRADE", 520f, startY + 13f, paint)

        // Table Rows
        var currentY = startY + 32f
        paint.color = Color.parseColor("#1E293B")
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        val rowTasks = tasks.take(30) // Display top items in single page summary
        for (t in rowTasks) {
            canvas.drawText(t.dayDateStr.take(15), 42f, currentY, paint)
            canvas.drawText(t.areaName, 130f, currentY, paint)
            canvas.drawText(t.scheduleTime, 240f, currentY, paint)
            canvas.drawText(t.status, 320f, currentY, paint)
            canvas.drawText(t.approvalBm, 400f, currentY, paint)
            canvas.drawText("${t.score}", 480f, currentY, paint)
            canvas.drawText(t.grade, 520f, currentY, paint)

            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawLine(36f, currentY + 4f, 559f, currentY + 4f, paint)
            paint.color = Color.parseColor("#1E293B")

            currentY += 18f
            if (currentY > 670f) break
        }

        // Footer & Formal Approval Signatures Block
        paint.color = Color.parseColor("#334155")
        paint.textSize = 9f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        val sigY = 730f
        // Left Column: Area Manager (Mengetahui)
        canvas.drawText("MENGETAHUI,", 60f, sigY, paint)
        canvas.drawText("AREA MANAGER", 60f, sigY + 14f, paint)

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = Color.parseColor("#64748B")
        canvas.drawText("( Tanda Tangan & Cap Digital )", 60f, sigY + 42f, paint)

        paint.color = Color.parseColor("#0F172A")
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Sanindo Setiawan", 60f, sigY + 70f, paint)
        paint.color = Color.parseColor("#475569")
        canvas.drawText("NIK: AM-8820192", 60f, sigY + 82f, paint)

        // Right Column: Building Manager (Menyetujui)
        paint.color = Color.parseColor("#0F172A")
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("MENYETUJUI,", 380f, sigY, paint)
        canvas.drawText("BUILDING MANAGER (BM)", 380f, sigY + 14f, paint)

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = Color.parseColor("#64748B")
        canvas.drawText("( Tanda Tangan & Cap Digital )", 380f, sigY + 42f, paint)

        paint.color = Color.parseColor("#0F172A")
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Fitria Nor Istiqomah", 380f, sigY + 70f, paint)
        paint.color = Color.parseColor("#475569")
        canvas.drawText("NIK: 2400713043", 380f, sigY + 82f, paint)

        // Bottom Footer Line
        paint.color = Color.parseColor("#CBD5E1")
        canvas.drawLine(36f, 810f, 559f, 810f, paint)

        paint.color = Color.parseColor("#94A3B8")
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Dokumen PDF ini dihasilkan secara resmi oleh Sistem Daily Performance Tracker - Benings Glow Clinic Gresik", 36f, 824f, paint)

        pdfDocument.finishPage(page)

        val cleanStart = startDateStr.replace("/", "-").replace("\\", "-").replace(" ", "_")
        val cleanEnd = endDateStr.replace("/", "-").replace("\\", "-").replace(" ", "_")
        val outputFile = File(context.cacheDir, "Laporan_Kinerja_${cleanStart}_${cleanEnd}.pdf")
        outputFile.parentFile?.mkdirs()
        FileOutputStream(outputFile).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()

        return outputFile
    }
}
