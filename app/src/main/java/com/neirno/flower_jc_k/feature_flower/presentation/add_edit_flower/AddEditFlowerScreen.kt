package com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.neirno.flower_jc_k.R
import com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.components.ActionSelectionDialog
import com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.components.TextWithBorder
import com.neirno.flower_jc_k.feature_flower.presentation.components.ButtonType
import com.neirno.flower_jc_k.feature_flower.presentation.components.CustomTopAppBar
import com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.components.TimeSliderDialog
import com.neirno.flower_jc_k.feature_flower.presentation.util.Screen
import com.neirno.flower_jc_k.ui.theme.CustomBlue
import com.neirno.flower_jc_k.ui.theme.CustomDark
import com.neirno.flower_jc_k.ui.theme.CustomGreen
import com.neirno.flower_jc_k.ui.theme.CustomWhite
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditFlowerScreen(
    navController: NavController,
    viewModel: AddEditFlowerViewModel = hiltViewModel()
) {
    val viewState = viewModel.viewState.value



    val context = LocalContext.current

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
        viewModel.onEvent(AddEditFlowerEvent.DeleteImage)
        navController.popBackStack()
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is  AddEditFlowerViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditFlowerViewModel.UiEvent.SaveFlower -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = CustomWhite,
        topBar = {
            CustomTopAppBar(
                modifier = Modifier,
                onLeftButtonClick = { navController.popBackStack() },
                leftButton = ButtonType.CLOSE,
                rightButton = ButtonType.ACCEPT,
                onRightButtonClick = {
                    viewModel.onEvent(AddEditFlowerEvent.SaveFlower)
                    /*viewModel.onEvent(AddEditFlowerEvent.SaveFlower)
                    navController.popBackStack()*/
                    Log.i("1", "Сохранен")
                }
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
                    .border(1.dp, CustomDark)
                    .clickable { navController.navigate(Screen.CameraScreen.route) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewState.flowerName.text,
                onValueChange = { newValue ->
                    viewModel.onEvent(AddEditFlowerEvent.EnteredName(newValue))
                },
                label = { Text(viewState.flowerName.hint) },
                modifier = Modifier.fillMaxWidth(),
                //isError = viewModel.flowerDescription.value.error.isNotBlank(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                //textStyle = Typography.bodyMedium.copy(color = CustomDark),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor= CustomDark,
                    unfocusedTextColor= CustomDark,
                    disabledTextColor= CustomDark,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = CustomDark
                    // ... другие цвета
                )
            )
            Divider(Modifier.offset(0.dp, -4.dp))

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewState.flowerDescription.text,
                onValueChange = { newValue ->
                    viewModel.onEvent(AddEditFlowerEvent.EnteredDescription(newValue))
                },
                label = { Text(viewState.flowerDescription.hint) },
                modifier = Modifier.fillMaxWidth(),
                //isError = viewModel.flowerDescription.value.error.isNotBlank(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor= CustomDark,
                    unfocusedTextColor= CustomDark,
                    disabledTextColor= CustomDark,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = CustomDark
                    // ... другие цвета
                )
            )
            Divider(Modifier.offset(0.dp, -4.dp))
            Spacer(modifier = Modifier.height(16.dp))

            // переменная для отслеживания видимости диалога
            var isTimerSliderDialogVisible by remember { mutableStateOf(false) }
            // переменная для хранения текущего типа действия
            var currentActionType by remember { mutableStateOf("") }
            var isDialogVisible by remember { mutableStateOf(false) }
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {isDialogVisible = !isDialogVisible}) {
                    Icon(modifier = Modifier.size(40.dp), contentDescription = ButtonType.ADD.description, painter = ButtonType.ADD.imageProvider())
                }

                ActionSelectionDialog(
                    showDialog = isDialogVisible,
                    selectedActions = viewState.selectedActions,
                    onCloseDialog = { isDialogVisible = false }
                ) { selectedAction ->
                    // Здесь отправим событие в ViewModel
                    viewModel.onEvent(AddEditFlowerEvent.SelectAction(selectedAction))
                }

                fun showTimerDialogForAction(actionType: String) {
                    currentActionType = actionType
                    isTimerSliderDialogVisible = true
                }

                fun formatTime(days: Int, hours: Int, minutes: Int): String {
                    val formattedHours = String.format("%02d", hours)
                    val formattedMinutes = String.format("%02d", minutes)
                    return "${context.getString(R.string.add_edit_every)}" +
                            " $days ${context.getString(R.string.add_edit_days_in)}" +
                            " $formattedHours:$formattedMinutes"
                }
                if ("WATERING" in viewState.selectedActions) {
                    TextWithBorder(
                        text = formatTime(viewState.flowerTimeToWater.days,
                            viewState.flowerTimeToWater.hours,
                            viewState.flowerTimeToWater.minutes),
                        borderColor = CustomBlue,
                        textColor = CustomDark,
                        iconTint = CustomBlue,
                        painter = ButtonType.WATER.imageProvider(),
                        onClick = { showTimerDialogForAction("WATERING") },
                        onDeleteClick = { viewModel.onEvent(AddEditFlowerEvent.RemoveAction("WATERING")) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if ("FERTILIZING" in viewState.selectedActions) {
                    TextWithBorder(
                        text = formatTime(viewState.flowerTimeToFertilize.days,
                            viewState.flowerTimeToFertilize.hours,
                            viewState.flowerTimeToFertilize.minutes),
                        borderColor = Color.Yellow,
                        textColor = CustomDark,
                        iconTint = Color.Yellow,
                        painter = ButtonType.FERTILIZE.imageProvider(),
                        onClick = { showTimerDialogForAction("FERTILIZING") },
                        onDeleteClick = { viewModel.onEvent(AddEditFlowerEvent.RemoveAction("FERTILIZING")) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if ("SPRAYING" in viewState.selectedActions) {
                    TextWithBorder(
                        text = formatTime(viewState.flowerTimeToSpraying.days,
                            viewState.flowerTimeToSpraying.hours,
                            viewState.flowerTimeToSpraying.minutes),
                        borderColor = CustomGreen,
                        textColor = CustomDark,
                        iconTint = CustomGreen,
                        painter = ButtonType.SPRAYING.imageProvider(),
                        onClick = { showTimerDialogForAction("SPRAYING") },
                        onDeleteClick = { viewModel.onEvent(AddEditFlowerEvent.RemoveAction("SPRAYING")) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                TimeSliderDialog(
                    isVisible = isTimerSliderDialogVisible,
                    onDismiss = { isTimerSliderDialogVisible = false },
                    // Определяем, какой именно таймер показывать, исходя из currentActionType
                    timer_d = when (currentActionType) {
                        "WATERING" -> viewState.flowerTimeToWater.days
                        "FERTILIZING" -> viewState.flowerTimeToFertilize.days
                        "SPRAYING" -> viewState.flowerTimeToSpraying.days
                        else -> 0
                    },
                    timer_h = when (currentActionType) {
                        "WATERING" -> viewState.flowerTimeToWater.hours
                        "FERTILIZING" -> viewState.flowerTimeToFertilize.hours
                        "SPRAYING" -> viewState.flowerTimeToSpraying.hours
                        else -> 0
                    },
                    timer_m = when (currentActionType) {
                        "WATERING" -> viewState.flowerTimeToWater.minutes
                        "FERTILIZING" -> viewState.flowerTimeToFertilize.minutes
                        "SPRAYING" -> viewState.flowerTimeToSpraying.minutes
                        else -> 0
                    },
                    onValueSelected = { days, hours, minutes ->
                        // Здесь отправляем событие в ViewModel, исходя из currentActionType
                        when (currentActionType) {
                            "WATERING" -> viewModel.onEvent(AddEditFlowerEvent.ChangeTimeToWater(days, hours, minutes))
                            "FERTILIZING" -> viewModel.onEvent(AddEditFlowerEvent.ChangeTimeToFertilize(days, hours, minutes))
                            "SPRAYING" -> viewModel.onEvent(AddEditFlowerEvent.ChangeTimeToSpraying(days, hours, minutes))
                        }
                    }
                )
            }
        }
    }
}
