package com.neirno.flower_jc_k.feature_flower.data.storage

import android.net.Uri

interface ImageStorageManager {
    fun saveImageToInternalStorage(uri: Uri): String?
    fun deleteImageFromInternalStorage(uri: Uri): Boolean
}
