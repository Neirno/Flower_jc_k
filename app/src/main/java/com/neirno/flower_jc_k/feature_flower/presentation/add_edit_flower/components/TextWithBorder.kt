package com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.neirno.flower_jc_k.feature_flower.presentation.components.ButtonType


@Composable
fun TextWithBorder(
    text: String,
    borderColor: Color,
    textColor: Color,
    iconTint: Color,
    painter: Painter?,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Левая часть: иконка и текст
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                painter?.let {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = it, contentDescription = null,
                        tint = iconTint
                    )
                }
                Text(
                    text = text,
                    color = textColor,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }
            // Правая часть: иконка "DELETE"
            IconButton(modifier = Modifier.size(25.dp), onClick = onDeleteClick) {
                Icon(
                    painter = ButtonType.DELETE.imageProvider(),
                    contentDescription = ButtonType.DELETE.description,
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}
