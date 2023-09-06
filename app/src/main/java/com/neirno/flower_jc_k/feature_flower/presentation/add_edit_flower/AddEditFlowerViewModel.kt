package com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neirno.flower_jc_k.feature_flower.background.checkAlarm
import com.neirno.flower_jc_k.feature_flower.background.setAlarm
import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import com.neirno.flower_jc_k.feature_flower.domain.model.InvalidFlowerException
import com.neirno.flower_jc_k.feature_flower.domain.use_case.FlowerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddEditFlowerViewModel @Inject constructor(
    private val flowerUseCases: FlowerUseCases,
    @ApplicationContext private val context: Context, // Внедрение контекста
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _viewState = mutableStateOf(AddEditFlowerViewState())
    val viewState: State<AddEditFlowerViewState> = _viewState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentFlowerId: Long? = null

    init {
        val flowerId: Long = when (val flowerIdObj = savedStateHandle.get<Any>("flowerId")) {
            is Int -> flowerIdObj.toLong()
            is Long -> flowerIdObj
            else -> -1L // или другое действие для неверного типа
        }
        if(flowerId != -1L) {
            viewModelScope.launch {
                flowerUseCases.getFlower(flowerId)?.also { flower ->
                    currentFlowerId = flower.id

                    val initialSelectedActions = mutableListOf<String>()

                    if (flower.wateringDays != 0) {
                        initialSelectedActions.add("WATERING")
                    }
                    if (flower.sprayingDays != 0) {
                        initialSelectedActions.add("SPRAYING")
                    }
                    if (flower.fertilizingDays != 0) {
                        initialSelectedActions.add("FERTILIZING")
                    }

                    _viewState.value = _viewState.value.copy(
                        flowerName = _viewState.value.flowerName.copy(
                            text = flower.name,
                            isHintVisible = false
                        ),
                        flowerDescription = _viewState.value.flowerDescription.copy(
                            text = flower.description ?: "",
                            isHintVisible = false
                        ),
                        flowerImageUri = Uri.parse("file:///" + (flower.imageFilePath ?: "")),
                        flowerTimeToWater = if (flower.wateringDays != 0) {
                            Time(flower.wateringDays, flower.wateringHours, flower.wateringMinutes)
                        } else {
                            _viewState.value.flowerTimeToWater // Значения по умолчанию
                        },
                        flowerTimeToSpraying = if (flower.sprayingDays != 0) {
                            Time(flower.sprayingDays, flower.sprayingHours, flower.sprayingMinutes)
                        } else {
                            _viewState.value.flowerTimeToSpraying // Значения по умолчанию
                        },
                        flowerTimeToFertilize = if (flower.fertilizingDays != 0) {
                            Time(flower.fertilizingDays, flower.fertilizingHours, flower.fertilizingMinutes)
                        } else {
                            _viewState.value.flowerTimeToFertilize // Значения по умолчанию
                        },
                        selectedActions = initialSelectedActions
                    )
                }
            }
        }
    }

    fun onEvent(event: AddEditFlowerEvent) {
        when(event) {
            is AddEditFlowerEvent.SelectAction -> {
                _viewState.value = _viewState.value.copy(
                    selectedActions = _viewState.value.selectedActions + event.str
                )
            }
            is AddEditFlowerEvent.RemoveAction -> {
                _viewState.value = _viewState.value.copy(
                    selectedActions = _viewState.value.selectedActions.filter { it != event.str }
                )
            }
            is AddEditFlowerEvent.EnteredName -> {
                _viewState.value = _viewState.value.copy(
                    flowerName = _viewState.value.flowerName.copy(
                        text = event.value
                    )
                )
            }
            is AddEditFlowerEvent.ChangeNameFocus -> {
                _viewState.value = _viewState.value.copy(
                    flowerName = _viewState.value.flowerName.copy(
                        isHintVisible = !event.focusState.isFocused &&
                                _viewState.value.flowerName.text.isBlank()
                    )
                )
            }
            is AddEditFlowerEvent.EnteredDescription -> {
                _viewState.value = _viewState.value.copy(
                    flowerDescription = _viewState.value.flowerDescription.copy(
                        text = event.value
                    )
                )
            }
            is AddEditFlowerEvent.ChangeDescriptionFocus -> {
                _viewState.value = _viewState.value.copy(
                    flowerDescription = _viewState.value.flowerDescription.copy(
                        isHintVisible = !event.focusState.isFocused &&
                                _viewState.value.flowerDescription.text.isBlank()
                    )
                )
            }
            is AddEditFlowerEvent.SetImage -> {
                _viewState.value = _viewState.value.copy(
                    flowerImageUri = event.uri
                )
            }
            is AddEditFlowerEvent.ChangeTimeToWater -> {
                _viewState.value = _viewState.value.copy(
                    flowerTimeToWater = Time(
                        days = event.days,
                        hours = event.hours,
                        minutes = event.minutes
                    )
                )
            }
            is AddEditFlowerEvent.ChangeTimeToFertilize -> {
                _viewState.value = _viewState.value.copy(
                    flowerTimeToFertilize = Time(
                        days = event.days,
                        hours = event.hours,
                        minutes = event.minutes
                    )
                )
            }
            is AddEditFlowerEvent.ChangeTimeToSpraying -> {
                _viewState.value = _viewState.value.copy(
                    flowerTimeToSpraying = Time(
                        days = event.days,
                        hours = event.hours,
                        minutes = event.minutes
                    )
                )
            }
            // Продолжите с другими событиями для свойств
            is AddEditFlowerEvent.SaveFlower -> {
                viewModelScope.launch {
                    try {
                        val imagePath = _viewState.value.flowerImageUri?.let { saveImageToInternalStorage(it) }
                        val selectedActions = _viewState.value.selectedActions

                        val flower = Flower(
                            id = currentFlowerId ?: 0L,
                            name = _viewState.value.flowerName.text,
                            description = _viewState.value.flowerDescription.text,
                            imageFilePath = imagePath,

                            wateringDays = if ("WATERING" in selectedActions) viewState.value.flowerTimeToWater.days else 0,
                            wateringHours = if ("WATERING" in selectedActions) viewState.value.flowerTimeToWater.hours else 0,
                            wateringMinutes = if ("WATERING" in selectedActions) viewState.value.flowerTimeToWater.minutes else 0,

                            fertilizingDays = if ("FERTILIZING" in selectedActions) viewState.value.flowerTimeToFertilize.days else 0,
                            fertilizingHours = if ("FERTILIZING" in selectedActions) viewState.value.flowerTimeToFertilize.hours else 0,
                            fertilizingMinutes = if ("FERTILIZING" in selectedActions) viewState.value.flowerTimeToFertilize.minutes else 0,

                            sprayingDays = if ("SPRAYING" in selectedActions) viewState.value.flowerTimeToSpraying.days else 0,
                            sprayingHours = if ("SPRAYING" in selectedActions) viewState.value.flowerTimeToSpraying.hours else 0,
                            sprayingMinutes = if ("SPRAYING" in selectedActions) viewState.value.flowerTimeToSpraying.minutes else 0
                        )
                        val oldFlower = currentFlowerId?.let { flowerUseCases.getFlower(it) }
                        var newFlower = flowerUseCases.addFlower(flower)

                        val actionTypeMapping = mapOf(
                            WATERING to "WATERING",
                            FERTILIZING to "FERTILIZING",
                            SPRAYING to "SPRAYING"
                        )

                        // Для каждого типа действия
                        for (actionType in listOf(WATERING, FERTILIZING, SPRAYING)) {
                            if (newFlower.needsUpdateForAction(oldFlower, actionType) && actionTypeMapping[actionType] in selectedActions) {
                                // Обновляем время следующего действия в базе данных
                                when (actionType) {
                                    WATERING -> flowerUseCases.updateWateringDate(newFlower)
                                    FERTILIZING -> flowerUseCases.updateFertilizingDate(newFlower)
                                    SPRAYING -> flowerUseCases.updateSprayingDate(newFlower)
                                }
                                newFlower = flowerUseCases.getFlower(newFlower.id)!!
                                // Устанавливаем новый будильник
                                setAlarm(context, newFlower, actionType)
                                Log.i("AddEdit newFlower watering", newFlower.nextWateringDateTime.toString())
                            } else {
                                // Скопируйте старую дату следующего действия, если она не изменилась
                                newFlower = newFlower.copyNextDateTimeFrom(oldFlower, actionType)
                                newFlower = flowerUseCases.addFlower(newFlower)
                            }
                        }
                        _eventFlow.emit(UiEvent.SaveFlower)
                    } catch(e: InvalidFlowerException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save flower"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveFlower: UiEvent()
    }

    fun Flower.needsUpdateForAction(oldFlower: Flower?, actionType: Int): Boolean {
        if (oldFlower == null) return true
        return when (actionType) {
            WATERING -> this.wateringDays != oldFlower.wateringDays ||
                    this.wateringHours != oldFlower.wateringHours ||
                    this.wateringMinutes != oldFlower.wateringMinutes
            FERTILIZING -> this.fertilizingDays != oldFlower.fertilizingDays ||
                    this.fertilizingHours != oldFlower.fertilizingHours ||
                    this.fertilizingMinutes != oldFlower.fertilizingMinutes
            SPRAYING -> this.sprayingDays != oldFlower.sprayingDays ||
                    this.sprayingHours != oldFlower.sprayingHours ||
                    this.sprayingMinutes != oldFlower.sprayingMinutes
            else -> false
        }
    }

    fun Flower.copyNextDateTimeFrom(oldFlower: Flower?, actionType: Int): Flower {
        if (oldFlower == null) return this
        return when (actionType) {
            WATERING -> this.copy(nextWateringDateTime = oldFlower.nextWateringDateTime)
            FERTILIZING -> this.copy(nextFertilizingDateTime = oldFlower.nextFertilizingDateTime)
            SPRAYING -> this.copy(nextSprayingDateTime = oldFlower.nextSprayingDateTime)
            else -> this
        }
    }

    /**
     * Сохраняет изображение во внутреннем хранилище приложения и возвращает путь к сохраненному файлу.
     *
     * @param uri URI изображения, которое нужно сохранить.
     * @return Путь к сохраненному файлу или null в случае ошибки.
     */
    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val filename = "flower_image_${System.currentTimeMillis()}.jpg"
            val destinationFilePath = "${context.filesDir}/$filename"

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            destinationFilePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }



    companion object {
        const val WATERING = 1
        const val FERTILIZING = 2
        const val SPRAYING = 3
    }
}