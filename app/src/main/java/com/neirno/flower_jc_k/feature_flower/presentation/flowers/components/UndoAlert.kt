/*
package com.neirno.flower_jc_k.feature_flower.presentation.flowers.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun UndoDialog(
    message: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    snackBarVisibilityState: MutableState<Boolean>
) {
    val timerProgress = remember { Animatable(1f) }
    LaunchedEffect(key1 = snackBarVisibilityState.value, block = {
        if (snackBarVisibilityState.value) {
            timerProgress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 10000, easing = LinearEasing)
            )
            if (timerProgress.value <= 0f) {
                snackBarVisibilityState.value = false
            }
        } else {
            delay(300) // Add delay here
            timerProgress.snapTo(1f)
        }
    })
    AnimatedVisibility(
        visible = snackBarVisibilityState.value,
        enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(durationMillis = 300)),
        exit = slideOutHorizontay(targetOffsetX = { -it }, animationSpec = tween(durationMillis = 300))
    ) {
        Card(
            modifier = modifier.padding(horizontal = 15.dp),
        ) {
            Column {
                CustomLinearProgressIndicator(progress = timerProgress.value, modifier = Modifier.fillMaxWidth().padding(3.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = message, modifier = Modifier.weight(1f))
                    Button(onClick = {
                        onButtonClick()
                        snackBarVisibilityState.value = false
                    }) {
                        Text(buttonText)ll
                    }
                }
            }
        }
    }
}*/
package com.neirno.flower_jc_k.feature_flower.presentation.flowers.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UndoDialog(
    modifier: Modifier = Modifier,
    message: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    durationMillis: Int = 5000,
) {
    val timerProgress = remember { Animatable(1f) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = isVisible) {
        if (isVisible) {
            timerProgress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis, easing = LinearEasing)
            )
            if (timerProgress.value <= 0f) {
                onDismiss()
            }
        } else {
            delay(300)
            timerProgress.snapTo(1f)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            coroutineScope.cancel()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(durationMillis = 300)),
        exit = slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(durationMillis = 300))
    ) {
        Card(
            modifier = modifier.padding(horizontal = 15.dp),
        ) {
            Column {
                CustomLinearProgressIndicator(progress = timerProgress.value, modifier = Modifier.fillMaxWidth().padding(3.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = message, modifier = Modifier.weight(1f))
                    Button(onClick = {
                        onButtonClick()
                        onDismiss()
                    }) {
                        Text(buttonText)
                    }
                }
            }
        }
    }
}
