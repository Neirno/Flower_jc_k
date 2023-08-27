package com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimePicker(
    selectedDay: Int,
    onDayChange: (Int) -> Unit,
    icon: Painter
) {
    val expanded = remember { mutableStateOf(false) }
    val days = (0..60).toList()

    val lineColor = getLineColor(selectedDay)

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 8.dp)) {
        Icon(
            painter = icon,
            contentDescription = "Time picker icon",
            modifier = Modifier.size(24.dp)
        )

        Box(modifier = Modifier.padding(start = 8.dp, end = 10.dp)) {

            SelectableLine(
                selectedDay = selectedDay,
                expanded = expanded,
                lineColor = lineColor
            )

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                days.forEach { day ->
                    DropdownMenuItem(
                        onClick = {
                            onDayChange(day)
                            expanded.value = false
                        },
                        text = { Text(day.toString()) })
                }
            }
        }
    }
}

@Composable
fun getLineColor(selectedDay: Int): Color {
    return when (selectedDay) {
        0 -> Color.Gray   // измените на нужный цвет
        else -> Color.Green  // измените на нужный цвет
    }
}


@Composable
fun SelectableLine(
    selectedDay: Int,
    expanded: MutableState<Boolean>,
    lineColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(lineColor)
            .clickable { expanded.value = true },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Selected day: $selectedDay",
            color = Color.DarkGray, // или любой другой цвет, который будет хорошо смотреться на фоне lineColor
            fontSize = 16.sp
        )
    }
}