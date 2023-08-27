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
                        _flowerName.value = flowerName.value.copy(
                            text = flower.name,
                            isHintVisible = false
                        )
                        _flowerDescription.value = flowerDescription.value.copy(
                            text = flower.description ?: "",
                            isHintVisible = false
                        )
                        flower.imageFilePath?.let { path ->
                            _flowerImageUri.value = Uri.parse("file:///"+path)
                        }
                        _flowerDaysToWater.value = flower.wateringFrequency
                        _flowerDaysToFertilize.value = flower.fertilizingFrequency
                        _flowerDaysToSpraying.value = flower.sprayingFrequency
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
                _flowerName.value = flowerName.value.copy(
                    text = event.value
                )
            }
            is AddEditFlowerEvent.ChangeNameFocus -> {
                _flowerName.value = flowerName.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            flowerName.value.text.isBlank()
                )
            }
            is AddEditFlowerEvent.EnteredDescription -> {
                _flowerDescription.value = _flowerDescription.value.copy(
                    text = event.value
                )
            }
            is AddEditFlowerEvent.ChangeDescriptionFocus -> {
                _flowerDescription.value = _flowerDescription.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _flowerDescription.value.text.isBlank()
                )
            }
            is AddEditFlowerEvent.SetImage -> {
                _flowerImageUri.value = event.uri
            }

            is AddEditFlowerEvent.ChangeDaysToWater -> {
                _flowerDaysToWater.value = event.days
            }
            is AddEditFlowerEvent.ChangeDaysToFertilize -> {
                _flowerDaysToFertilize.value = event.days
            }
            is AddEditFlowerEvent.ChangeDaysToSpraying -> {
                _flowerDaysToSpraying.value = event.days
            }
            is AddEditFlowerEvent.ChangeMinutesToWater -> {
                _flowerMinutesToWater.value = event.hours * 60 + event.minutes
            }
            is AddEditFlowerEvent.ChangeMinutesToFertilize -> {
                _flowerMinutesToFertilize.value = event.hours * 60 + event.minutes
            }
            is AddEditFlowerEvent.ChangeMinutesToSpraying -> {
                _flowerMinutesToSpraying.value = event.hours * 60 + event.minutes
            }
            // Продолжите с другими событиями для свойств
            is AddEditFlowerEvent.SaveFlower -> {
                viewModelScope.launch {
                    try {
                        val imagePath = _flowerImageUri.value?.let { saveImageToInternalStorage(it) }

                        if (imagePath != null) {
                            Log.i("ImagePath", imagePath)
                        }
                        val flower = Flower(
                            id = currentFlowerId ?: 0L,
                            //name = viewState.value.flowerTimeToSpraying,
                            name = flowerName.value.text,
                            description = flowerDescription.value.text,
                            imageFilePath = imagePath,
                            wateringFrequency = flowerDaysToWater.value,
                            fertilizingFrequency = flowerDaysToFertilize.value,
                            sprayingFrequency = flowerDaysToSpraying.value,
                            wateringTime = flowerMinutesToWater.value,
                            fertilizingTime = flowerMinutesToFertilize.value,
                            spayingTime = flowerMinutesToSpraying.value

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
            WATERING -> flower.wateringFrequency
            FERTILIZING -> flower.fertilizingFrequency
            SPRAYING -> flower.sprayingFrequency
            else -> throw IllegalArgumentException("Invalid action type")
        }

        val minutes = when (actionType) {
            WATERING -> flower.wateringTime
            FERTILIZING -> flower.fertilizingTime
            SPRAYING -> flower.spayingTime
            else -> throw IllegalArgumentException("Invalid action type")
        }

        val nextDateTime = calculateNextActionTime(minutes, days)

        // Получите обновленный объект Flower на основе actionType
        val updatedFlower = getUpdatedFlowerForAction(flower, actionType, nextDateTime)

        // Сохраните обновленный объект Flower
        return flowerUseCases.addFlower(updatedFlower)
    }


    private fun calculateNextActionTime(minutes: Int, days: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, days + 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
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