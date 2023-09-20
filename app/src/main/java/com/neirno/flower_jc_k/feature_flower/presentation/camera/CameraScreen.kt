package com.neirno.flower_jc_k.feature_flower.presentation.camera

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.common.util.concurrent.ListenableFuture
import com.neirno.flower_jc_k.feature_flower.presentation.camera.components.CameraControls
import com.neirno.flower_jc_k.feature_flower.presentation.camera.components.CanvasPhoto
import com.neirno.flower_jc_k.feature_flower.presentation.components.ButtonType
import com.neirno.flower_jc_k.feature_flower.presentation.components.CustomTopAppBar
import java.io.File

@Composable
fun CameraScreen(
    navController: NavController,
    viewState: CameraState,
    onEvent: (CameraEvent) -> Unit
) {
    val context: Context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val preview: Preview = remember { Preview.Builder().build() }
    val cameraSelector = remember { CameraSelector.DEFAULT_BACK_CAMERA }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }

    BackHandler() {
        if (viewState.isInPreviewMode) {
            onEvent(CameraEvent.UpdatePreviewImageUri(null))
            onEvent(CameraEvent.SetIsInPreviewMode(false))
        } else {
            navController.popBackStack()
        }
    }

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                if (uri != null) {
                    onEvent(CameraEvent.UpdatePreviewImageUri(uri))
                    onEvent(CameraEvent.SetIsInPreviewMode(true))
                }
            }
        }

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CameraControls(
                    isInPreviewMode = viewState.isInPreviewMode,
                    onCapture = {
                        takePhoto(context, imageCapture) { uri ->
                            onEvent(CameraEvent.UpdatePreviewImageUri(uri))
                            onEvent(CameraEvent.SetIsInPreviewMode(true))
                        }
                    },
                    onOpenGallery = {
                        val intent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startForResult.launch(intent)
                    },
                    onAccept = {
                        Log.d("NavControllerDebug", "Before setting imageUri")
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "imageUri", viewState.previewImageUri
                        )
                        navController.popBackStack()
                    }
                )
            }
        }
    ) { paddingValues: PaddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues),
        ) {

            if (viewState.isInPreviewMode && viewState.previewImageUri != null) {
                Image(
                    painter = rememberImagePainter(viewState.previewImageUri),
                    contentDescription = "Preview",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                CustomTopAppBar(
                    Modifier
                        .align(Alignment.TopCenter)
                        .background(Color.Transparent),
                    leftButton = ButtonType.BACK,
                    onLeftButtonClick = {
                        onEvent(CameraEvent.UpdatePreviewImageUri(null))
                        onEvent(CameraEvent.SetIsInPreviewMode(false))
                    }
                )
            } else {
                CameraView(
                    viewState = viewState,
                    onEvent = onEvent,
                    cameraProviderFuture = cameraProviderFuture,
                    preview = preview,
                    imageCapture = imageCapture,
                    cameraSelector = cameraSelector
                )
                CustomTopAppBar(
                    Modifier
                        .align(Alignment.TopCenter)
                        .background(Color.Transparent),
                    leftButton = ButtonType.CLOSE,
                    onLeftButtonClick = { navController.popBackStack() },
                    onRightButtonClick = {
                        onEvent(CameraEvent.SetFlashlightState)
                    },
                    rightButton = if (viewState.isFlashlightOn) ButtonType.FLASH_ON else ButtonType.FLASH_OFF
                )
                CanvasPhoto(Modifier)
            }
        }
    }
}


private fun takePhoto(context: Context, imageCapture: ImageCapture, onPhotoTaken: (Uri) -> Unit) {
    val fileName = "flower_image_${System.currentTimeMillis()}.jpg"
    val outputDirectory = context.getExternalFilesDir(null)
    val photoFile = File(outputDirectory, fileName)

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                onPhotoTaken(savedUri)
            }

            override fun onError(exc: ImageCaptureException) {
                Log.e("CameraScreen", "Photo capture failed: ${exc.message}", exc)
            }
        }
    )
}

@SuppressLint("ClickableViewAccessibility")
@Composable
private fun CameraView(
    viewState: CameraState,
    onEvent: (CameraEvent) -> Unit,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    preview: Preview,
    imageCapture: ImageCapture,
    cameraSelector: CameraSelector,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val focusPoint = viewState.focusPoint
    val showFocusPoint = viewState.showFocusPoint

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                val previewView = PreviewView(context)
                previewView.setOnTouchListener { _, event ->
                    Log.d("CameraView", "Tapped at: x = ${event.x}, y = ${event.x}")
                    onEvent(CameraEvent.SetFocus(event.x, event.y))
                    true
                }
                cameraProviderFuture.addListener({
                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                    try {
                        // Очищаем все предыдущие использования камеры перед повторным использованием
                        cameraProvider.unbindAll()
                        val camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview, imageCapture
                        )
                        onEvent(CameraEvent.SetCamera(camera))
                        preview.setSurfaceProvider(previewView.surfaceProvider)
                    } catch (exc: Exception) {
                        Log.e("CameraView", "Use case binding failed", exc)
                    }
                }, ContextCompat.getMainExecutor(context))
                previewView
            },
        )
        FocusAnimation(focusPoint, showFocusPoint)
    }
}

@Composable
private fun FocusAnimation(focusPoint: Pair<Float, Float>, showFocusPoint: Boolean) {
    val animatedRadius = remember { Animatable(initialValue = 10f) }
    val strokeWidth = 4f  // ширина ободка
    LaunchedEffect(showFocusPoint) {
        if (showFocusPoint) {
            // Увеличение размера круга до 40f
            animatedRadius.animateTo(
                targetValue = 60f,
                animationSpec = tween(200, easing = LinearOutSlowInEasing)
            )
            // Уменьшение размера круга до 20f
            animatedRadius.animateTo(
                targetValue = 0f,
                animationSpec = tween(200, easing = LinearOutSlowInEasing)
            )
        }
    }

    if (showFocusPoint) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawCircle(
                color = Color.White,
                radius = animatedRadius.value,
                center = Offset(focusPoint.first, focusPoint.second),
                style = Stroke(width = strokeWidth)
            )
        }
    }
}

