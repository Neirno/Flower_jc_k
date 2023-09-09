package com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.neirno.flower_jc_k.feature_flower.presentation.components.ButtonType
import com.neirno.flower_jc_k.R
import com.neirno.flower_jc_k.ui.theme.CustomBlue
import com.neirno.flower_jc_k.ui.theme.CustomBrown
import com.neirno.flower_jc_k.ui.theme.CustomDark
import com.neirno.flower_jc_k.ui.theme.CustomGreen

@Composable
fun ActionSelectionDialog(
    showDialog: Boolean,
    selectedActions: List<String>,
    onCloseDialog: () -> Unit,
    onActionSelected: (String) -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onCloseDialog) {
            // Содержимое диалога
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(CustomBrown)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.choose_action))
                
                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                ActionButton(
                    actionName = stringResource(id = R.string.water),
                    painter = ButtonType.WATER.imageProvider(),
                    isEnabled = "WATERING" !in selectedActions,
                    onClick = {
                        onActionSelected("WATERING")
                        onCloseDialog()
                    },
                    tint = CustomBlue
                )

                ActionButton(
                    actionName = stringResource(id = R.string.fertilize),
                    painter = ButtonType.FERTILIZE.imageProvider(),
                    isEnabled = "FERTILIZING" !in selectedActions,
                    onClick = {
                        onActionSelected("FERTILIZING")
                        onCloseDialog()
                    },
                    tint = Color.Yellow
                )

                ActionButton(
                    actionName = stringResource(id = R.string.spraying),
                    painter = ButtonType.SPRAYING.imageProvider(),
                    isEnabled = "SPRAYING" !in selectedActions,
                    onClick = {
                        onActionSelected("SPRAYING")
                        onCloseDialog()
                    },
                    tint = CustomGreen
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    actionName: String,
    painter: Painter?,
    isEnabled: Boolean,
    onClick: () -> Unit,
    tint: Color
) {
    if (isEnabled) {
        TextButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()  // Если нужно, чтобы кнопка заполняла всю доступную ширину
                .height(50.dp)  // Высота кнопки
                .background(Color.Transparent)
                /* .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(0.dp)
                )*/
                .padding(0.dp) // Убрать внутренние отступы
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(actionName, color = CustomDark)
                painter?.let {
                    Icon(painter = it, contentDescription = null, tint = tint)
                }
            }
        }
    }
}
