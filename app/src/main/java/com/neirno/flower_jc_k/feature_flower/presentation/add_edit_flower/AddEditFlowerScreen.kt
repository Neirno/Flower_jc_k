package com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower

import android.app.Activity
import android.app.TimePickerDialog
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composer
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.neirno.flower_jc_k.R
import com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.components.MyApp
import com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.components.SlidingTimeSelection
import com.neirno.flower_jc_k.feature_flower.presentation.components.ButtonType
import com.neirno.flower_jc_k.feature_flower.presentation.components.CustomTopAppBar
import com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.components.TimePicker
import com.neirno.flower_jc_k.feature_flower.presentation.util.Screen
import kotlin.math.abs

@Composable
fun AddEditFlowerScreen(
    navController: NavController,
    viewModel: AddEditFlowerViewModel = hiltViewModel()
) {
    val existingImagePath by viewModel.flowerImageUri // Получение пути изображения из ViewModel

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val imageUri: Uri? = savedStateHandle?.get("imageUri")
    if (imageUri != null)
        viewModel.onEvent(AddEditFlowerEvent.SetImage(imageUri))

    val painter = rememberImagePainter(
        data = existingImagePath,
        builder = {
            crossfade(true)
            error(R.drawable.ic_help) // предоставьте свое изображение-заглушку
            fallback(R.drawable.ic_home) // предоставьте свое изображение-заглушку
        }
    )

    BackHandler() {
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                modifier = Modifier,
                onLeftButtonClick = {navController.popBackStack()},
                leftButton = ButtonType.CLOSE,
            )
        },

    ) { contentPadding ->
        Column (
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Image(
                painter = painter,
                contentDescription = "Flower Image",
                contentScale = ContentScale.Crop, // Вот этот параметр обеспечивает обрезку и масштабирование
                modifier = Modifier
                    .size(256.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, MaterialTheme.colorScheme.primary)
                    .clickable { navController.navigate(Screen.CameraScreen.route) }
            )

            OutlinedTextField(
                value = viewModel.flowerName.value.text,
                onValueChange = { newValue ->
                    viewModel.onEvent(AddEditFlowerEvent.EnteredName(newValue))
                },
                label = { Text(viewModel.flowerName.value.hint) },
                modifier = Modifier.fillMaxWidth(),
                //isError = viewModel.flowerName.value.error.isNotBlank(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = viewModel.flowerDescription.value.text,
                onValueChange = { newValue ->
                    viewModel.onEvent(AddEditFlowerEvent.EnteredDescription(newValue))
                },
                label = { Text(viewModel.flowerDescription.value.hint) },
                modifier = Modifier.fillMaxWidth(),
                //isError = viewModel.flowerDescription.value.error.isNotBlank(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(16.dp))
            var selectedDays by remember {mutableStateOf(0)}
            //DaysSelector(selectedDays, { value -> selectedDays = value})
            var selectedHour by remember { mutableStateOf(0) }
            var selectedMinute by remember { mutableStateOf(0) }
            Text("$selectedDays $selectedHour $selectedMinute")

            var isDialogVisible by remember { mutableStateOf(false) }

            TimeSliderDialog(
                isVisible = isDialogVisible,
                onDismiss = {isDialogVisible = !isDialogVisible},
                timer_d = 3,
                timer_h = 12,
                timer_m = 0,
                onValueSelected = { hours, minutes, days ->
                run {
                    viewModel.onEvent(AddEditFlowerEvent.ChangeMinutesToWater(hours, minutes))
                    viewModel.onEvent(AddEditFlowerEvent.ChangeDaysToWater(days))
                }
            })


            IconButton(onClick = {isDialogVisible = !isDialogVisible}) {
                Icon(modifier = Modifier, contentDescription = "", imageVector = Icons.Default.Add)
            }

            TimePicker(
                selectedDay = viewModel.flowerDaysToWater.value,
                onDayChange = { day -> viewModel.onEvent(AddEditFlowerEvent.ChangeDaysToWater(day)) },
                icon = painterResource(id = R.drawable.ic_flower_list)
            )

            TimePicker(
                selectedDay = viewModel.flowerDaysToFertilize.value,
                onDayChange = { day -> viewModel.onEvent(AddEditFlowerEvent.ChangeDaysToFertilize(day)) },
                icon = painterResource(id = R.drawable.ic_flower_list)
            )

            // Assuming there's a similar TimePicker for daysToSpraying
            TimePicker(
                selectedDay = viewModel.flowerDaysToSpraying.value,
                onDayChange = { day -> viewModel.onEvent(AddEditFlowerEvent.ChangeDaysToSpraying(day)) },
                icon = painterResource(id = R.drawable.ic_flower_list)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                viewModel.onEvent(AddEditFlowerEvent.SaveFlower)
                navController.popBackStack()
                Log.i("1", "Сохранен")
            }) {
                Text(text = "Add Flower")
            }

        }
    }
}

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
                            Column (Modifier.align(CenterVertically)){
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