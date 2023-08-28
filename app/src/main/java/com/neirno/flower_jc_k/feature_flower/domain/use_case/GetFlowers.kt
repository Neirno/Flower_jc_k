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
                        is FlowerOrder.Water -> flowers.sortedBy { it.wateringDays }
                        is FlowerOrder.Fertilize -> flowers.sortedBy { it.fertilizingDays }
                        is FlowerOrder.Spraying -> flowers.sortedBy { it.sprayingDays }
                    }
                }
                is OrderType.Descending -> {
                    when(flowerOrder) {
                        is FlowerOrder.Name -> flowers.sortedByDescending { it.name.lowercase() }
                        //is FlowerOrder.Date -> flowers.sortedByDescending { it.timestamp }
                        is FlowerOrder.Water -> flowers.sortedByDescending { it.wateringDays }
                        is FlowerOrder.Fertilize -> flowers.sortedByDescending { it.fertilizingDays }
                        is FlowerOrder.Spraying -> flowers.sortedByDescending { it.sprayingDays }
                    }
                }
            }
        }
    }
}