package com.neirno.flower_jc_k.feature_flower.presentation.camera.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun CanvasPhoto(
    modifier: Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .size(300.dp)
        ) {
            val lineWidth = 60.dp.toPx()
            val strokeWidth = 2.dp.toPx()
            val cornerRadius = 20.dp.toPx()

            val stroke = Stroke(strokeWidth)

            // Верхний левый угол
            drawLine(
                Color.White,
                Offset(cornerRadius, 0f),
                Offset(lineWidth, 0f),
                strokeWidth
            )
            drawLine(
                Color.White,
                Offset(0f, cornerRadius),
                Offset(0f, lineWidth),
                strokeWidth
            )
            drawArc(
                Color.White,
                -90f,
                -90f,
                useCenter = false,
                topLeft = Offset(0f, 0f),
                size = Size(cornerRadius * 2, cornerRadius * 2),
                style = stroke
            )

            // Верхний правый угол
            drawLine(
                Color.White,
                Offset(size.width - cornerRadius, 0f),
                Offset(size.width - lineWidth, 0f),
                strokeWidth
            )
            drawLine(
                Color.White,
                Offset(size.width, cornerRadius),
                Offset(size.width, lineWidth),
                strokeWidth
            )
            drawArc(
                Color.White,
                0f,
                -90f,
                useCenter = false,
                topLeft = Offset(size.width - cornerRadius * 2, 0f),
                size = Size(cornerRadius * 2, cornerRadius * 2),
                style = stroke
            )

            // Нижний левый угол
            drawLine(
                Color.White,
                Offset(cornerRadius, size.height),
                Offset(lineWidth, size.height),
                strokeWidth
            )
            drawLine(
                Color.White,
                Offset(0f, size.height - cornerRadius),
                Offset(0f, size.height - lineWidth),
                strokeWidth
            )
            drawArc(
                Color.White,
                180f,
                -90f,
                useCenter = false,
                topLeft = Offset(0f, size.height - cornerRadius * 2),
                size = Size(cornerRadius * 2, cornerRadius * 2),
                style = stroke
            )

            // Нижний правый угол
            drawLine(
                Color.White,
                Offset(size.width - cornerRadius, size.height),
                Offset(size.width - lineWidth, size.height),
                strokeWidth
            )
            drawLine(
                Color.White,
                Offset(size.width, size.height - cornerRadius),
                Offset(size.width, size.height - lineWidth),
                strokeWidth
            )
            drawArc(
                Color.White,
                90f,
                -90f,
                useCenter = false,
                topLeft = Offset(
                    size.width - cornerRadius * 2,
                    size.height - cornerRadius * 2
                ),
                size = Size(cornerRadius * 2, cornerRadius * 2),
                style = stroke
            )
        }
    }
}