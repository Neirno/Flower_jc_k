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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.neirno.flower_jc_k.R
import com.neirno.flower_jc_k.ui.theme.CustomBrown
import com.neirno.flower_jc_k.ui.theme.CustomDark

@Composable
fun TimeSliderDialog(
    isVisible: Boolean,
    title: String = "Выберите время",
    timer_d: Int, timer_h: Int, timer_m: Int,
    onValueSelected: (days: Int, hours: Int, minutes: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var _timer_h by remember { mutableIntStateOf(timer_h) }
    var _timer_m by remember { mutableIntStateOf(timer_m) }
    var _timer_d by remember { mutableIntStateOf(timer_d) }

    if (isVisible) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(CustomBrown)
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
                        text = stringResource(id = R.string.timer_slider_notification),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(Modifier.fillMaxWidth()) {
                        // Часть для выбора дней
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = stringResource(id = R.string.days), color = Color(0xFF333733), style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            SlidingTimeSelection(
                                modifier = Modifier.height(75.dp),
                                text = "",
                                minValue = 1,
                                maxValue = 60,
                                callBack = { _timer_d = it },
                                defaultValue = timer_d
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Часть для выбора часов и минут
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column {
                                Text(text = stringResource(id = R.string.hours), color = Color(0xFF203620), style = MaterialTheme.typography.labelMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                SlidingTimeSelection(
                                    modifier = Modifier.height(75.dp),
                                    text = "",
                                    maxValue = 23,
                                    callBack = { _timer_h = it },
                                    defaultValue = timer_h
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
                                Text(text = stringResource(id = R.string.minutes), maxLines = 1, color = Color(0xFF203620), style = MaterialTheme.typography.labelMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                SlidingTimeSelection(
                                    modifier = Modifier.height(75.dp),
                                    text = "",
                                    maxValue = 59,
                                    callBack = { _timer_m = it },
                                    defaultValue = timer_m
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
                            Text(text = stringResource(id = R.string.cancel), color = CustomDark)
                        }
                        TextButton(onClick = {
                            onValueSelected(_timer_d, _timer_h, _timer_m)
                            onDismiss()
                        }) {
                            Text(text = stringResource(id = R.string.ok), color = CustomDark)
                        }
                    }
                }
            }
        }
    }
}