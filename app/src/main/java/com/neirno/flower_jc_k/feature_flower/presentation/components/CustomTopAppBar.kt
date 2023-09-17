package com.neirno.flower_jc_k.feature_flower.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.neirno.flower_jc_k.ui.theme.CustomDark


@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    leftButton: ButtonType? = null,
    onLeftButtonClick: (() -> Unit)? = null,
    tintLeft: Color? = null, // Доделать
    rightButton: ButtonType? = null,
    onRightButtonClick: (() -> Unit)? = null,
    tintRight: Color? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),

        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leftButton != null) {
                Icon(
                    painter = leftButton.imageProvider(),
                    contentDescription = leftButton.description,
                    modifier = modifier
                        .padding(start = 16.dp)
                        .clickable(onClick = { onLeftButtonClick?.invoke() })
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (rightButton != null) {
                Icon(
                    painter = rightButton.imageProvider(),
                    contentDescription = rightButton.description,
                    modifier = modifier
                        .padding(end = 16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onRightButtonClick?.invoke() }
                )
            }

        }
    }
}
