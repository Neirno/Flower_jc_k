package com.neirno.flower_jc_k.feature_flower.domain.use_case

import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import com.neirno.flower_jc_k.feature_flower.domain.repository.FlowerRepository
import com.neirno.flower_jc_k.feature_flower.domain.util.FlowerOrder
import com.neirno.flower_jc_k.feature_flower.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFlowers(
    private val repository: FlowerRepository
) {

    operator fun invoke(
        flowerOrder: FlowerOrder = FlowerOrder.Water(OrderType.Descending)
    ): Flow<List<Flower>> {
        return repository.getFlowers().map { flowers ->
            when(flowerOrder.orderType) {
                is OrderType.Ascending -> {
                    when(flowerOrder) {
                        is FlowerOrder.Name -> flowers.sortedBy { it.name.lowercase() }
                        //is FlowerOrder.Date -> flowers.sortedBy { it.timestamp }
                        is FlowerOrder.Water -> flowers.sortedBy { it.wateringFrequency }
                        is FlowerOrder.Fertilize -> flowers.sortedBy { it.fertilizingFrequency }
                        is FlowerOrder.Spraying -> flowers.sortedBy { it.sprayingFrequency }
                    }
                }
                is OrderType.Descending -> {
                    when(flowerOrder) {
                        is FlowerOrder.Name -> flowers.sortedByDescending { it.name.lowercase() }
                        //is FlowerOrder.Date -> flowers.sortedByDescending { it.timestamp }
                        is FlowerOrder.Water -> flowers.sortedByDescending { it.wateringFrequency }
                        is FlowerOrder.Fertilize -> flowers.sortedByDescending { it.fertilizingFrequency }
                        is FlowerOrder.Spraying -> flowers.sortedByDescending { it.sprayingFrequency }
                    }
                }
            }
        }
    }
}