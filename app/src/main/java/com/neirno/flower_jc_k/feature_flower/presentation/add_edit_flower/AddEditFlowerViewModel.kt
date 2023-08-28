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
        savedStateHandle.get<Int>("flowerId")?.let { flowerId ->
            if(flowerId != -1) {
                viewModelScope.launch {
                    flowerUseCases.getFlower(flowerId)?.also { flower ->
                        currentFlowerId = flower.id
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
                            flowerTimeToWater = Time(
                                flower.wateringDays,
                                flower.wateringHours,
                                flower.wateringMinutes
                            ),
                            flowerTimeToSpraying = Time(
                                flower.sprayingDays,
                                flower.sprayingHours,
                                flower.sprayingMinutes
                            ),
                            flowerTimeToFertilize = Time(
                                flower.fertilizingDays,
                                flower.fertilizingHours,
                                flower.fertilizingMinutes
                            )

                        )
                    }
                }
            }
        }
      /*  savedStateHandle.get<Uri>("imageUri")?.let { imageUri ->
            Log.d("ViewModelDebug", "Received imageUri: $imageUri")
            _flowerImageUri.value = imageUri
        } ?: Log.d("ViewModelDebug", "No imageUri found in savedStateHandle")*/
    }


    fun onEvent(event: AddEditFlowerEvent) {
        when(event) {
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
          /*  is AddEditFlowerEvent.ChangeDaysToWater -> {
                _viewState.value = _viewState.value.copy(
                    flowerDaysToWater = event.days
                )
            }
            is AddEditFlowerEvent.ChangeDaysToFertilize -> {
                _viewState.value = _viewState.value.copy(
                    flowerDaysToFertilize = event.days
                )
            }
            is AddEditFlowerEvent.ChangeDaysToSpraying -> {
                _viewState.value = _viewState.value.copy(
                    flowerDaysToSpraying = event.days
                )
            }
            is AddEditFlowerEvent.ChangeMinutesToWater -> {
                _viewState.value = _viewState.value.copy(
                    flowerMinutesToWater = event.hours * 60 + event.minutes
                )
            }
            is AddEditFlowerEvent.ChangeMinutesToFertilize -> {
                _viewState.value = _viewState.value.copy(
                    flowerMinutesToFertilize = event.hours * 60 + event.minutes
                )
            }
            is AddEditFlowerEvent.ChangeMinutesToSpraying -> {
                _viewState.value = _viewState.value.copy(
                    flowerMinutesToSpraying = event.hours * 60 + event.minutes
                )
            }*/
            // Продолжите с другими событиями для свойств
            is AddEditFlowerEvent.SaveFlower -> {
                viewModelScope.launch {
                    try {
                        val imagePath = _viewState.value.flowerImageUri?.let { saveImageToInternalStorage(it) }

                        val flower = Flower(
                            id = currentFlowerId ?: 0L,
                            name = _viewState.value.flowerName.text,
                            description = _viewState.value.flowerDescription.text,
                            imageFilePath = imagePath,

                            wateringDays = viewState.value.flowerTimeToWater.days,
                            wateringHours = viewState.value.flowerTimeToWater.hours,
                            wateringMinutes = viewState.value.flowerTimeToWater.minutes,

                            fertilizingDays = viewState.value.flowerTimeToFertilize.days,
                            fertilizingHours = viewState.value.flowerTimeToFertilize.hours,
                            fertilizingMinutes = viewState.value.flowerTimeToFertilize.minutes,

                            sprayingDays = viewState.value.flowerTimeToSpraying.days,
                            sprayingHours = viewState.value.flowerTimeToSpraying.hours,
                            sprayingMinutes = viewState.value.flowerTimeToSpraying.minutes

                        )

                        var newFlower = flowerUseCases.addFlower(flower)

                        // Установите будильники и обновите цветок после успешного сохранения
                        newFlower = setAlarmAndUpdateFlower(newFlower, WATERING)
                        newFlower = setAlarmAndUpdateFlower(newFlower, FERTILIZING)
                        setAlarmAndUpdateFlower(newFlower, SPRAYING)

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

    private suspend fun setAlarmAndUpdateFlower(flower: Flower, actionType: Int): Flower {
        // Установите будильник
        setAlarm(context, flower, actionType)
        if (checkAlarm(context, flower, actionType))
            Log.i("CheckAlarm", "Work")

        // Вычислите следующую дату и время для действия
        val days = when (actionType) {
            WATERING -> flower.wateringDays
            FERTILIZING -> flower.fertilizingDays
            SPRAYING -> flower.sprayingDays
            else -> throw IllegalArgumentException("Invalid action type")
        }

        val hours = when (actionType) {
            WATERING -> flower.wateringHours
            FERTILIZING -> flower.fertilizingHours
            SPRAYING -> flower.sprayingHours
            else -> throw IllegalArgumentException("Invalid action type")
        }

        val minutes = when (actionType) {
            WATERING -> flower.wateringMinutes
            FERTILIZING -> flower.fertilizingMinutes
            SPRAYING -> flower.sprayingMinutes
            else -> throw IllegalArgumentException("Invalid action type")
        }

        val nextDateTime = calculateNextActionTime(days, hours, minutes)

        // Получите обновленный объект Flower на основе actionType
        val updatedFlower = getUpdatedFlowerForAction(flower, actionType, nextDateTime)

        // Сохраните обновленный объект Flower
        return flowerUseCases.addFlower(updatedFlower)
    }


    private fun calculateNextActionTime(days: Int, hours: Int, minutes: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, days + 1)
        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, minutes)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }





    private fun getUpdatedFlowerForAction(flower: Flower, actionType: Int, nextDateTime: Long): Flower {
        return when (actionType) {
            1 -> flower.copy(nextWateringDateTime = nextDateTime)
            2 -> flower.copy(nextFertilizingDateTime = nextDateTime)
            3 -> flower.copy(nextSprayingDateTime = nextDateTime)
            else -> throw IllegalArgumentException("Invalid action type")
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val filename = "flower_image_${System.currentTimeMillis()}.jpg"
        val outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)

        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream?.close()

        return "${context.filesDir}/$filename"
    }

    companion object {
        const val WATERING = 1
        const val FERTILIZING = 2
        const val SPRAYING = 3
    }
}