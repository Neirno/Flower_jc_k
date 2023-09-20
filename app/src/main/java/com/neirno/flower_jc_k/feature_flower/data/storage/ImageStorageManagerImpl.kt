package com.neirno.flower_jc_k.feature_flower.data.storage

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.neirno.flower_jc_k.feature_flower.domain.use_case.FlowerUseCases
import com.neirno.flower_jc_k.feature_flower.domain.use_case.GetFlowers
import com.neirno.flower_jc_k.feature_flower.domain.util.FlowerOrder
import com.neirno.flower_jc_k.feature_flower.domain.util.OrderType
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class ImageStorageManagerImpl (
    private val context: Context,
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

    // трабл, не могу понять почему сохраняются Img в приватной части внешнего хранилища,а не внутри
    // туду разобраться поч так
    override fun cleanupUnusedImages(usedImagePaths: Set<String>) {
        cleanupDirectory(context.filesDir, usedImagePaths)  // Внутреннее хранилище
        context.getExternalFilesDir(null)?.let {
            cleanupDirectory(it, usedImagePaths)  // Внешнее хранилище
        }
    }

    private fun cleanupDirectory(directory: File, usedImagePaths: Set<String>) {
        val allFiles = directory.listFiles()?.toList() ?: emptyList()
        Log.i("DirectoryPath", directory.absolutePath)
        Log.i("cleanupUnusedImages", allFiles.size.toString())

        for (file in allFiles) {
            // Если файл не используется, удаляем его
            if (!usedImagePaths.contains(file.absolutePath)) {
                try {
                    file.delete()
                } catch (e: Exception) {
                    Log.e("FileDeletionError", "Error deleting file: ${file.absolutePath}", e)
                }
            }
        }
    }

}
