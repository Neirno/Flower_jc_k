package com.neirno.flower_jc_k.feature_flower.domain.repository

import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import kotlinx.coroutines.flow.Flow

interface FlowerRepository {

    fun getFlowers(): Flow<List<Flower>>

    suspend fun getFlowerById(id: Int): Flower?

    suspend fun insertFlower(flower: Flower): Long

    suspend fun updateFlower(flower: Flower)

    suspend fun deleteFlower(flower: Flower)

    suspend fun updateWateringDate(flower: Flower)

    suspend fun updateFertilizingDate(flower: Flower)

    suspend fun updateSprayingDate(flower: Flower)

}
