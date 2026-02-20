package com.mentaltrack.ai.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.mentaltrack.ai.data.model.MoodEntry
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class DataExporter(private val context: Context) {
    
    fun exportToCsv(entries: List<MoodEntry>, fileName: String = "mood_entries.csv"): Uri? {
        try {
            // Create a temporary file in the app's cache directory
            val file = File(context.cacheDir, fileName)
            
            FileWriter(file).use { writer ->
                // Write CSV header
                writer.appendLine("ID,Timestamp,Mood Level,Activities,Note")
                
                // Write each entry
                entries.forEach { entry ->
                    val activitiesStr = entry.activities.joinToString("|")
                    writer.appendLine("${entry.id},${entry.timestamp},${entry.moodLevel},${activitiesStr},${entry.note}")
                }
            }
            
            // Return the URI for sharing
            return FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    
    fun shareFile(uri: Uri, subject: String = "MentalTrack AI Data Export") {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "text/csv"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        
        val chooserIntent = Intent.createChooser(shareIntent, "Share data file")
        context.startActivity(chooserIntent)
    }
    
    // Helper method to format timestamp
    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}