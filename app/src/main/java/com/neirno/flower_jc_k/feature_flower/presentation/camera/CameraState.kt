package com.neirno.flower_jc_k.feature_flower.presentation.camera

import android.net.Uri
import androidx.camera.core.Camera

data class CameraState(
    val previewImageUri: Uri? = null,
    val isInPreviewMode: Boolean = false,
    val isFlashlightOn: Boolean = false,
    val camera: Camera? = null,
    val focusPoint: Pair<Float, Float> = Pair(0f,0f),
    val showFocusPoint: Boolean = false
)
