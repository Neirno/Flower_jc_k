package com.neirno.flower_jc_k.feature_flower.presentation.flowers

import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import com.neirno.flower_jc_k.feature_flower.domain.util.FlowerOrder
import com.neirno.flower_jc_k.feature_flower.domain.util.OrderType

data class OrderState(
    val flowers: List<Flower> = emptyList(),
    val flowerOrder: FlowerOrder = FlowerOrder.Water(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
)
