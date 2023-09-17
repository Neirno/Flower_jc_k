package com.neirno.flower_jc_k.feature_flower.presentation.flowers

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import com.neirno.flower_jc_k.feature_flower.domain.use_case.FlowerUseCases
import com.neirno.flower_jc_k.feature_flower.domain.util.FlowerOrder
import com.neirno.flower_jc_k.feature_flower.domain.util.OrderType
import com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.AddEditFlowerViewModel
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
            is FlowersEvent.UpdateWater -> handleFlowerEventUpdate(WATERING, event.flowers)
            is FlowersEvent.UpdateFertilize -> handleFlowerEventUpdate(FERTILIZING, event.flowers)
            is FlowersEvent.UpdateSpray -> handleFlowerEventUpdate(SPRAYING, event.flowers)

            is FlowersEvent.DeleteFlower -> {
                viewModelScope.launch {
                    for (flower in event.flowers) {
                        flower.imageFilePath?.let { flowerUseCases.deleteImage(Uri.parse(it))}

                        if (flowerUseCases.checkAlarmForFlower(flower, WATERING))
                            flowerUseCases.cancelAlarmForFlower(flower, WATERING)

                        if (flowerUseCases.checkAlarmForFlower(flower, FERTILIZING))
                            flowerUseCases.cancelAlarmForFlower(flower, FERTILIZING)

                        if (flowerUseCases.checkAlarmForFlower(flower, SPRAYING))
                            flowerUseCases.cancelAlarmForFlower(flower, SPRAYING)

                        flowerUseCases.deleteFlower(flower)
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

    private fun handleFlowerEventUpdate(actionType: Int, flowers: List<Flower>) {
        viewModelScope.launch {
            for (flower in flowers) {
                if (actionType == WATERING && flowerUseCases.checkAlarmForFlower(flower, actionType)) {
                    flowerUseCases.cancelAlarmForFlower(flower, actionType)
                }

                when (actionType) {
                    WATERING -> flowerUseCases.updateWateringDate(flower)
                    FERTILIZING -> flowerUseCases.updateFertilizingDate(flower)
                    SPRAYING -> flowerUseCases.updateSprayingDate(flower)
                }

                val newFlower = flowerUseCases.getFlower(flower.id)!!

                flowerUseCases.setAlarmForFlower(newFlower, actionType)

                if (actionType == WATERING) {
                    if (flowerUseCases.checkAlarmForFlower(newFlower, actionType)) {
                        Log.d("Alarm in updateWater", "Set on ${newFlower.nextWateringDateTime}")
                    } else {
                        Log.e("Alarm in updateWater", "Error set alarm in flower ${flower.name}, id ${flower.id}")
                    }
                }
            }
        }
    }
    companion object {
        const val WATERING = 1
        const val FERTILIZING = 2
        const val SPRAYING = 3
    }
}
