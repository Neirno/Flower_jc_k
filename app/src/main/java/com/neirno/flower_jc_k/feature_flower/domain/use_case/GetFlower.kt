package com.neirno.flower_jc_k.feature_flower.domain.use_case

import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import com.neirno.flower_jc_k.feature_flower.domain.repository.FlowerRepository

class GetFlower(
    private val repository: FlowerRepository
) {

    suspend operator fun invoke(id: Int): Flower? {
        return repository.getFlowerById(id)
    }
}