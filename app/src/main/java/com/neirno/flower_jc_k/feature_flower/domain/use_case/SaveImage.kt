package com.neirno.flower_jc_k.feature_flower.domain.use_case

import android.net.Uri
import com.neirno.flower_jc_k.feature_flower.data.storage.ImageStorageManager

class SaveImage(
    private val imageStorageManager: ImageStorageManager
) {
    operator fun invoke(uri: Uri): String? {
        return imageStorageManager.saveImageToInternalStorage(uri)
    }
}
