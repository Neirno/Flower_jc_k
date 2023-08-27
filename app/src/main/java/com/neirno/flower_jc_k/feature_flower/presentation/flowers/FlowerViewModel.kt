package com.neirno.flower_jc_k.feature_flower.presentation.flowers

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import com.neirno.flower_jc_k.feature_flower.domain.use_case.FlowerUseCases
import com.neirno.flower_jc_k.feature_flower.domain.util.FlowerOrder
import com.neirno.flower_jc_k.feature_flower.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

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
                        //recentlyChangedFlowers.add(flower)
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
       /*     is FlowersEvent.SelectFlower -> {
                viewModelScope.launch {
                    if (event.flower in currentSelectedItems) {
                        currentSelectedItems.remove(event.flower)
                    } else {
                        currentSelectedItems.add(event.flower)
                        _viewState.value.selectedFlowers + event.flower
                    }
                    _viewState.value = _viewState.value.copy(selectedFlowers = currentSelectedItems.toList())
                }
            }*/
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
           /* is FlowersEvent.ClearRecentlyDeletedFlowers -> {
                viewModelScope.launch {
                    //recentlyChangedFlowers.clear()
                }
            }*/
            is FlowersEvent.SetActiveOperation -> {
                viewModelScope.launch {
                    _viewState.value = _viewState.value.copy(activeOperation = event.operation)
                }
            }
        /*    is FlowersEvent.RestoreFlower -> {
                viewModelScope.launch {
                    while (recentlyChangedFlowers.isNotEmpty()) {
                        val flowerToRestore = recentlyChangedFlowers.removeLast()
                        flowerUseCases.addFlower(flowerToRestore)
                    }
                }
            }*/
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
        /*    is FlowersEvent.ShowUndoDialog -> {
                _viewState.value = _viewState.value.copy(showConfirm = true)
            }
            is FlowersEvent.HideUndoDialog -> {
                _viewState.value = _viewState.value.copy(showConfirm = false)
            }*/
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
}
