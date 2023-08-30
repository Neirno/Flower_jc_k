package com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.neirno.flower_jc_k.R
import com.neirno.flower_jc_k.feature_flower.presentation.components.ButtonType
import com.neirno.flower_jc_k.feature_flower.presentation.components.CustomTopAppBar
import com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.components.TimeSliderDialog
import com.neirno.flower_jc_k.feature_flower.presentation.util.Screen

@Composable
fun AddEditFlowerScreen(
    navController: NavController,
    viewModel: AddEditFlowerViewModel = hiltViewModel()
) {
    val viewState = viewModel.viewState.value

    val existingImagePath = viewState.flowerImageUri // Получение пути изображения из ViewModel

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
        containerColor = Color(0xFFDBD2C0),
        topBar = {
            CustomTopAppBar(
                modifier = Modifier,
                onLeftButtonClick = { navController.popBackStack() },
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
                value = viewState.flowerName.text,
                onValueChange = { newValue ->
                    viewModel.onEvent(AddEditFlowerEvent.EnteredName(newValue))
                },
                label = { Text(viewState.flowerName.hint) },
                modifier = Modifier.fillMaxWidth(),
                //isError = viewModel.flowerName.value.error.isNotBlank(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = viewState.flowerDescription.text,
                onValueChange = { newValue ->
                    viewModel.onEvent(AddEditFlowerEvent.EnteredDescription(newValue))
                },
                label = { Text(viewState.flowerDescription.hint) },
                modifier = Modifier.fillMaxWidth(),
                //isError = viewModel.flowerDescription.value.error.isNotBlank(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(16.dp))

            var isDialogVisible by remember { mutableStateOf(false) }
            var isTimerSliderDialogVisible by remember { mutableStateOf(false) }
            TimeSliderDialog(
                isVisible = isTimerSliderDialogVisible,
                onDismiss = {isTimerSliderDialogVisible = !isTimerSliderDialogVisible},
                timer_d = viewState.flowerTimeToWater.days,
                timer_h = viewState.flowerTimeToWater.hours,
                timer_m = viewState.flowerTimeToWater.minutes,
                onValueSelected = { hours, minutes, days ->
                    run {
                        viewModel.onEvent(AddEditFlowerEvent.ChangeTimeToWater(days, hours, minutes))
                    }
                }
            )




            Column () {
                IconButton(onClick = {isDialogVisible = !isDialogVisible}) {
                    Icon(modifier = Modifier, contentDescription = "", imageVector = Icons.Default.Add)
                }

                ActionSelectionDialog(
                    showDialog = isDialogVisible,
                    selectedActions = viewState.selectedActions,
                    onCloseDialog = { isDialogVisible = false }
                ) { selectedAction ->
                    // Здесь отправим событие в ViewModel
                    viewModel.onEvent(AddEditFlowerEvent.SelectAction(selectedAction))
                }

                if ("WATERING" in viewState.selectedActions) {
                    Text(
                        text = "${viewState.flowerTimeToWater.days}:" +
                                "${viewState.flowerTimeToWater.hours}:" +
                                "${viewState.flowerTimeToWater.minutes}",
                        modifier = Modifier.clickable { isTimerSliderDialogVisible = !isTimerSliderDialogVisible }
                    )
                }

                if ("FERTILIZING" in viewState.selectedActions) {
                    Text(text = "ГотовоФ")
                    // Показать информацию и поля для удобрения
                }

                if ("SPRAYING" in viewState.selectedActions) {
                    Text(text = "ГотовоС")
                    // Показать информацию и поля для опрыскивания
                }
            }


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
fun ActionSelectionDialog(
    showDialog: Boolean,
    selectedActions: List<String>,
    onCloseDialog: () -> Unit,
    onActionSelected: (String) -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onCloseDialog) {
            // Содержимое диалога
            Column {
                Text(text = "Выберите действие")

                Spacer(modifier = Modifier.height(16.dp))
                if ("WATERING" !in selectedActions) {
                    Button(onClick = {
                        onActionSelected("WATERING")
                        onCloseDialog()
                    }) {
                        Text("Добавить полив")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if ("FERTILIZING" !in selectedActions) {
                    Button(onClick = {
                        onActionSelected("FERTILIZING")
                        onCloseDialog()
                    }) {
                        Text("Добавить удобрение")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if ("FERTILIZING" !in selectedActions) {
                    Button(onClick = {
                        onActionSelected("SPRAYING")
                        onCloseDialog()
                    }) {
                        Text("Добавить опрыскивание")
                    }
                }
            }
        }
    }
}
