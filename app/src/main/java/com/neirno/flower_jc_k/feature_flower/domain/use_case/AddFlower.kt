package com.neirno.flower_jc_k.feature_flower.domain.use_case

import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import com.neirno.flower_jc_k.feature_flower.domain.model.InvalidFlowerException
import com.neirno.flower_jc_k.feature_flower.domain.repository.FlowerRepository

class AddFlower(
    private val repository: FlowerRepository
    ) {
    @Throws(InvalidFlowerException::class)
    suspend operator fun invoke(flower: Flower): Flower {
        if(flower.name.isBlank()) {
            throw InvalidFlowerException("The name of the note can't be empty.")
        }
        /*if(flower.description.isBlank()) {
            throw InvalidNoteException("The description of the note can't be empty.")
        }*/
        val id = repository.insertFlower(flower)
        return flower.copy(id = id)
    }
}
