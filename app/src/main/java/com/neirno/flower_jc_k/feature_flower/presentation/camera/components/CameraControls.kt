package com.neirno.flower_jc_k.feature_flower.presentation.camera.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.neirno.flower_jc_k.feature_flower.presentation.components.ButtonType


@Composable
fun CameraControls(
    isInPreviewMode: Boolean,
    onCapture: () -> Unit,
    onOpenGallery: () -> Unit,
    onAccept: () -> Unit
) {
    if (isInPreviewMode) {
        IconButton(
            onClick = { onAccept() },
            modifier = Modifier
                .size(72.dp))
        {
            Icon(
                painter = ButtonType.ACCEPT.imageProvider(),
                contentDescription = ButtonType.ACCEPT.description,
                modifier = Modifier
                    .size(72.dp),
                tint = Color.Black
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            // Кнопка "Сделать фото" по центру
            IconButton(
                onClick = onCapture,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(72.dp)
            ) {
                Icon(Icons.Default.Camera, modifier = Modifier.size(72.dp), contentDescription = "Сделать фото", tint = Color.Black)
            }
            // Кнопка "Галерея" слева снизу от кнопки "Сделать фото"
            IconButton(
                onClick = onOpenGallery,
                modifier = Modifier
                    .offset(y = -8.dp)
                    .align(Alignment.BottomStart)
                    .size(36.dp)
            ) {
                Icon(Icons.Default.Image, modifier = Modifier.size(36.dp), contentDescription = "Открыть галерею", tint = Color.Black)
            }
        }
    }
}
