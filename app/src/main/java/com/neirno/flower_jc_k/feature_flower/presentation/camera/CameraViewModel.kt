package com.neirno.flower_jc_k.feature_flower.presentation.camera

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(): ViewModel() {
    private val _viewState = mutableStateOf(CameraState())
    val viewState: State<CameraState> = _viewState

    val previousImage = mutableStateOf<Uri?>(null)

    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.UpdatePreviewImageUri -> {
                viewModelScope.launch {
                    // Удаление предыдущего изображения
                    previousImage.value?.let { deleteImage(it) }
                    previousImage.value = event.uri
                    _viewState.value = _viewState.value.copy(previewImageUri = previousImage.value)
                }
            }
            is CameraEvent.SetIsInPreviewMode -> {
                viewModelScope.launch {
                    _viewState.value = _viewState.value.copy(isInPreviewMode = event.isInPreview)
                }
            }
            is CameraEvent.SetFlashlightState -> {
                viewModelScope.launch {
                    val newState = !_viewState.value.isFlashlightOn
                    _viewState.value = _viewState.value.copy(isFlashlightOn = newState)
                    try {
                        _viewState.value.camera?.cameraControl?.enableTorch(newState)
                    } catch (e: Exception) {
                        Log.e("Flash light error: ", e.message.toString())
                    }
                }
            }
            is CameraEvent.SetCamera -> {
                viewModelScope.launch {
                    _viewState.value = _viewState.value.copy(camera = event.camera)
                }
            }
        }
    }

    private fun deleteImage(uri: Uri) {
        try {
            val file = uri.path?.let { File(it) }
            if (file != null) {
                if (file.exists()) {
                    file.delete()
                }
            } else {
                Log.w("Delete Image warning (null):", uri.toString())
            }
        } catch (e: Exception) {
            Log.e("Delete Image error:", e.message.toString())
        }
    }
}