package com.neirno.flower_jc_k.feature_flower.presentation.camera

import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraInfoUnavailableException
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neirno.flower_jc_k.feature_flower.domain.use_case.FlowerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val flowerUseCases: FlowerUseCases,
): ViewModel() {
    private val _viewState = mutableStateOf(CameraState())
    val viewState: State<CameraState> = _viewState

    val previousImage = mutableStateOf<Uri?>(null)

    private val focusChannel = Channel<Pair<Float, Float>>(Channel.UNLIMITED)
    private var currentFocusJob: Job? = null

    init {
        processFocusEvents(focusChannel)
    }

    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.UpdatePreviewImageUri -> {
                viewModelScope.launch {
                    // Удаление предыдущего изображения
                    previousImage.value?.let { flowerUseCases.deleteImage(it) }
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
            is CameraEvent.SetFocus -> {
                focusChannel.trySendBlocking(Pair(event.x, event.y))
            }
        }
    }

    private fun processFocusEvents(focusEvents: ReceiveChannel<Pair<Float, Float>>) {
        viewModelScope.launch {
            var lastFocusTime = 0L
            val debounceTime = 400L // время в миллисекундах
            var lastFocusPoint: Pair<Float, Float>? = null

            for (focusPoint in focusEvents) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastFocusTime >= debounceTime) {
                    lastFocusTime = currentTime
                    currentFocusJob?.cancel()
                    currentFocusJob = launch {
                        setFocus(focusPoint.first, focusPoint.second)
                    }
                } else {
                    lastFocusPoint = focusPoint
                }
            }

            // Обработка последней сохраненной точки фокусировки после окончания дебаунсинга
            lastFocusPoint?.let {
                delay(debounceTime)
                setFocus(it.first, it.second)
            }
        }
    }


    private fun setFocus(x: Float, y: Float) {

        val newState = _viewState.value.copy(
            focusPoint = Pair(x, y),
            showFocusPoint = true
        )
        _viewState.value = newState

        val autoFocusPoint = SurfaceOrientedMeteringPointFactory(
            1f, // ширина превью
            1f  // высота превью
        ).createPoint(x, y)

        val action = FocusMeteringAction.Builder(autoFocusPoint).build()

        try {
            _viewState.value.camera?.cameraControl?.startFocusAndMetering(action)
        } catch (e: CameraInfoUnavailableException) {
            Log.e("Focus error", e.toString())
        }
        viewModelScope.launch {
            delay(400)  // например, через 0.4 секунды
            _viewState.value = _viewState.value.copy(showFocusPoint = false)
        }
    }
}