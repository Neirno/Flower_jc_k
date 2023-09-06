package com.neirno.flower_jc_k.feature_flower.presentation.flowers

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neirno.flower_jc_k.feature_flower.background.cancelAlarm
import com.neirno.flower_jc_k.feature_flower.background.checkAlarm
import com.neirno.flower_jc_k.feature_flower.background.setAlarm
import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import com.neirno.flower_jc_k.feature_flower.domain.use_case.FlowerUseCases
import com.neirno.flower_jc_k.feature_flower.domain.util.FlowerOrder
import com.neirno.flower_jc_k.feature_flower.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File

@HiltViewModel
class FlowerViewModel @Inject constructor(
    @ApplicationContext private val context: Context, // Внедрение контекста
    private val flowerUseCases: FlowerUseCases
) : ViewModel() {

    private var currentSelectedItems: MutableList<Flower> = mutableListOf()
//    private var recentlyChangedFlowers: MutableList<Flower> = mutableListOf()

    private val _viewState = mutableStateOf(FlowerViewState())
    val viewState: State<FlowerViewState> = _viewState

    private var getFlowersJob: Job? = null

    init {
        getFlowers(FlowerOrder.Water(OrderType.Descending))
    }

    fun onEvent(event: FlowersEvent) {
        when(event) {
            is FlowersEvent.Order -> {
                if (_viewState.value.orderState.flowerOrder::class == event.flowerOrder::class &&
                    _viewState.value.orderState.flowerOrder.orderType == event.flowerOrder.orderType
                ) {
                    return
                }
                getFlowers(event.flowerOrder)
            }
            is FlowersEvent.DeleteFlower -> {
                viewModelScope.launch {
                    for (flower in event.flowers) {
                        flower.imageFilePath?.let { deleteImageFromInternalStorage(it) }
                        flowerUseCases.deleteFlower(flower)
                        //recentlyChangedFlowers.add(flower)

                    }
                }
            }
            is FlowersEvent.UpdateWater -> {
                viewModelScope.launch {
                    for (flower in event.flowers) {
                        flowerUseCases.updateWateringDate(flower)
                        val newFlower = flowerUseCases.getFlower(flower.id)!!
                        cancelAlarm(context, newFlower.id, WATERING) // 1 — это код для полива

                        checkAlarm(context, newFlower, WATERING)
                        // Устанавливаем новый будильник
                        setAlarm(context, newFlower, WATERING) // 1 — это код для полива

                    }
                }
            }
            is FlowersEvent.UpdateFertilize -> {
                viewModelScope.launch {
                    for (flower in event.flowers) {
                        flowerUseCases.updateFertilizingDate(flower)
                        //recentlyChangedFlowers.add(flower)
                    }
                }
            }
            is FlowersEvent.UpdateSpray -> {
                viewModelScope.launch {
                    for (flower in event.flowers) {
                        flowerUseCases.updateSprayingDate(flower)
                        //recentlyChangedFlowers.add(flower)
                    }
                }
            }
            is FlowersEvent.SelectFlower -> {
                viewModelScope.launch {
                    val newSelectedFlowers = if (event.flower in _viewState.value.selectedFlowers) {
                        _viewState.value.selectedFlowers.filterNot { it == event.flower }
                    } else {
                        _viewState.value.selectedFlowers + event.flower
                    }
                    _viewState.value = _viewState.value.copy(selectedFlowers = newSelectedFlowers)
                }
            }

            is FlowersEvent.ClearSelectedFlower -> {
                viewModelScope.launch {
                    //currentSelectedItems.clear()
                    _viewState.value = _viewState.value.copy(selectedFlowers = emptyList())
                }
            }
            is FlowersEvent.SetActiveOperation -> {
                viewModelScope.launch {
                    _viewState.value = _viewState.value.copy(activeOperation = event.operation)
                }
            }
            is FlowersEvent.ToggleOrderSection -> {
                _viewState.value = _viewState.value.copy(
                    orderState = _viewState.value.orderState.copy(
                        isOrderSectionVisible = !_viewState.value.orderState.isOrderSectionVisible
                    )
                )
            }
            is FlowersEvent.ShowConfirmationDialog -> {
                _viewState.value = _viewState.value.copy(showConfirmationDialog = true)
            }
            is FlowersEvent.HideConfirmationDialog -> {
                _viewState.value = _viewState.value.copy(showConfirmationDialog = false)
            }
        }
    }

    private fun getFlowers(flowerOrder: FlowerOrder) {
        getFlowersJob?.cancel()
        getFlowersJob = flowerUseCases.getFlowers(flowerOrder)
            .onEach { flowers ->
                _viewState.value = _viewState.value.copy(
                    orderState = _viewState.value.orderState.copy(
                        flowers = flowers,
                        flowerOrder = flowerOrder
                    )
                )
            }
            .launchIn(viewModelScope)
    }

    private fun deleteImageFromInternalStorage(imagePath: String) {
        val file = File(imagePath)
        if (file.exists()) {
            file.delete()
        }
    }

    companion object {
        const val WATERING = 1
        const val FERTILIZING = 2
        const val SPRAYING = 3
    }
}
