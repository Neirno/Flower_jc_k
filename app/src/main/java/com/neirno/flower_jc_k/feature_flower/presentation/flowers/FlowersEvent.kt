package com.neirno.flower_jc_k.feature_flower.presentation.flowers

import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import com.neirno.flower_jc_k.feature_flower.domain.util.FlowerOrder

sealed class FlowersEvent {
    data class Order(val flowerOrder: FlowerOrder): FlowersEvent()
    data class DeleteFlower(val flowers: List<Flower>): FlowersEvent()
    data class UpdateWater(val flowers: List<Flower>): FlowersEvent()
    data class UpdateFertilize(val flowers: List<Flower>): FlowersEvent()
    data class UpdateSpray(val flowers: List<Flower>): FlowersEvent()
    data class SelectFlower(val flower: Flower): FlowersEvent()
    data class SetActiveOperation(val operation: ActiveOperation): FlowersEvent()

    object ClearSelectedFlower: FlowersEvent()
    object ToggleOrderSection: FlowersEvent()
    object ShowConfirmationDialog : FlowersEvent()
    object HideConfirmationDialog : FlowersEvent()
}