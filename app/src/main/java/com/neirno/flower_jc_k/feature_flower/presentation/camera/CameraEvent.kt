package com.neirno.flower_jc_k.feature_flower.presentation.camera

import android.net.Uri
import androidx.camera.core.Camera

sealed class CameraEvent {
    data class UpdatePreviewImageUri(val uri: Uri?) : CameraEvent()
    data class SetIsInPreviewMode(val isInPreview: Boolean) : CameraEvent()
    data class SetCamera(val camera: Camera?) : CameraEvent()
    data class SetFocus(val x: Float, val y: Float) : CameraEvent()

    object SetFlashlightState : CameraEvent()

}
