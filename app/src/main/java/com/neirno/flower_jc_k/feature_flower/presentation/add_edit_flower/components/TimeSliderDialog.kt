package com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TimeSliderDialog(
    isVisible: Boolean,
    title: String = "Выберите время",
    timer_d: Int, timer_h: Int, timer_m: Int,
    onValueSelected: (hours: Int, minutes: Int, days: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var _timer_h by remember { mutableIntStateOf(timer_h) }
    var _timer_m by remember { mutableIntStateOf(timer_m) }
    var _timer_d by remember { mutableIntStateOf(timer_d) }

    if (isVisible) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFC0DFC1))
                .padding(16.dp))

            {
                Column {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        text = title,
                        color = Color(0xFF804030),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        text = "Получайте уведомления каждые выбранные дни в заданное время.",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(Modifier.fillMaxWidth()) {
                        // Часть для выбора дней
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Дни", color = Color(0xFF333733), style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            SlidingTimeSelection(
                                modifier = Modifier.height(75.dp),
                                text = "",
                                minValue = 1,
                                maxValue = 60,
                                callBack = { _timer_d = it },
                                defaultValue = _timer_d
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Часть для выбора часов и минут
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column {
                                Text(text = "Часы", color = Color(0xFF203620), style = MaterialTheme.typography.labelMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                SlidingTimeSelection(
                                    modifier = Modifier.height(75.dp),
                                    text = "",
                                    maxValue = 23,
                                    callBack = { _timer_h = it },
                                    defaultValue = _timer_h
                                )
                            }
                            Column (Modifier.align(Alignment.CenterVertically)){
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(horizontal = 8.dp),
                                    text = ":",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }


                            Column {
                                Text(text = "Мин", maxLines = 1, color = Color(0xFF203620), style = MaterialTheme.typography.labelMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                SlidingTimeSelection(
                                    modifier = Modifier.height(75.dp),
                                    text = "",
                                    maxValue = 59,
                                    callBack = { _timer_m = it },
                                    defaultValue = _timer_m
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { onDismiss() }) {
                            Text("Cancel")
                        }
                        TextButton(onClick = {
                            onValueSelected(_timer_h, _timer_m, _timer_d)
                            onDismiss()
                        }) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}