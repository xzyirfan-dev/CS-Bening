package com.example.data.remote

import android.util.Log
import com.example.data.local.ChecklistTaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GoogleSheetSyncService {
    private const val WEB_APP_URL = "https://script.google.com/macros/s/AKfycbyu6-n7sAloSefwvgJSdMRBwBGPTC6ZYdx0yd9NZzg-X3qgS8h8Ze6lxwrTouHQK_Y7/exec"
    private const val GDRIVE_FOLDER_URL = "https://drive.google.com/drive/folders/1C0uiaHQAmtI0pTil1leuEsqYA4vhOTt7"

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .build()

    suspend fun syncTaskToGoogleSheet(actionType: String, task: ChecklistTaskEntity): Boolean = withContext(Dispatchers.IO) {
        try {
            val json = JSONObject().apply {
                put("action", actionType) // "SUBMIT_CS_PHOTO", "BM_APPROVAL", "BM_UPDATE_SCHEDULE"
                put("taskId", task.id)
                put("dayDateStr", task.dayDateStr)
                put("dayNum", task.dayNum)
                put("monthYearStr", task.monthYearStr)
                put("session", task.session)
                put("areaName", task.areaName)
                put("scheduleTime", task.scheduleTime)
                put("picName", task.picName)
                put("estimateMinutes", task.estimateMinutes)
                put("status", task.status)
                put("approvalBm", task.approvalBm)
                put("grade", task.grade)
                put("score", task.score)
                put("catatanBm", task.catatanBm)
                put("photoTimestamp", task.photoTimestamp ?: "")
                put("gpsLocation", task.gpsLocation ?: "")
                put("photoUrl", task.photoUrl ?: "")
                put("gdriveFolderUrl", GDRIVE_FOLDER_URL)
            }

            val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url(WEB_APP_URL)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string()
                Log.d("GoogleSheetSync", "Response ($actionType): ${response.code} - $bodyString")
                return@withContext response.isSuccessful
            }
        } catch (e: Exception) {
            Log.e("GoogleSheetSync", "Failed to sync task $actionType to Google Sheet", e)
            return@withContext false
        }
    }
}
