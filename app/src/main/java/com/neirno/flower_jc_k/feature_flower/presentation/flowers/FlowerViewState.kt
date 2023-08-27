package com.neirno.flower_jc_k.feature_flower.presentation.flowers

import com.neirno.flower_jc_k.feature_flower.domain.model.Flower


data class FlowerViewState(
    val orderState: OrderState = OrderState(),
    val selectedFlowers: List<Flower> = emptyList(),
    val activeOperation: ActiveOperation = ActiveOperation.NONE,
    val showConfirmationDialog: Boolean = false,
    val showConfirm: Boolean = false
)
