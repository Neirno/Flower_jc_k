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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/*

@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    buttonType: ButtonType? = null,
    onButtonClick: (() -> Unit)? = null,
    buttonSettings: ButtonType? = null,
    onClickSettings: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        buttonType?.let {
            IconButton(
                onClick = { onButtonClick?.invoke() }
            ) {
                Image(
                    painter = painterResource(it.image),
                    contentDescription = it.description
                )
            }
        }

        Text(
            text = "Название приложения",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        buttonSettings?.let {
            IconButton(
                onClick = { onClickSettings?.invoke() },
            ) {
                Image(
                    painter = painterResource(it.image),
                    contentDescription = it.description,
                )
            }
        }
    }
    Divider()
}
*/
@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    leftButton: ButtonType? = null,
    onLeftButtonClick: (() -> Unit)? = null,
    rightButton: ButtonType? = null,
    onRightButtonClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
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
                    imageVector = leftButton.image,
                    contentDescription = leftButton.description,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable(onClick = { onLeftButtonClick?.invoke() })
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            // Проверяем, предоставлена ли функция для управления фонариком
            // Внутри вашего CustomTopBar
            if (rightButton != null) {
                Icon(
                    imageVector = rightButton.image,
                    contentDescription = rightButton.description,
                    modifier = Modifier
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
