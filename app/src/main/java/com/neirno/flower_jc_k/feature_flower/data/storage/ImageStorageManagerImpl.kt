package com.neirno.flower_jc_k.feature_flower.data.storage

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File

class ImageStorageManagerImpl(
    private val context: Context
) : ImageStorageManager {
    override fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val filename = "flower_image_${System.currentTimeMillis()}.jpg"
            val destinationFilePath = "${context.filesDir}/$filename"

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            destinationFilePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun deleteImageFromInternalStorage(uri: Uri): Boolean {
        return try {
            val file = File(uri.path!!)
            if (file.exists()) {
                file.delete()
                true
            } else {
                Log.w("Delete Image warning:", "File does not exist: ${uri.toString()}")
                false
            }
        } catch (e: Exception) {
            Log.e("Delete Image error:", e.message.toString())
            false
        }
    }

}
