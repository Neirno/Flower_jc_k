package com.neirno.flower_jc_k.feature_flower.domain.use_case

import com.neirno.flower_jc_k.R
import com.neirno.flower_jc_k.di.FlowerModule.ResourceProvider
import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import com.neirno.flower_jc_k.feature_flower.domain.model.InvalidFlowerException
import com.neirno.flower_jc_k.feature_flower.domain.repository.FlowerRepository

class AddFlower(
    private val repository: FlowerRepository,
    private val resourceProvider: ResourceProvider
    ) {
    @Throws(InvalidFlowerException::class)
    suspend operator fun invoke(flower: Flower): Flower {
        if(flower.name.isBlank()) {
            throw InvalidFlowerException(resourceProvider.getString(R.string.error_name))
        }
        /*if(flower.description.isBlank()) {
            throw InvalidNoteException("The description of the note can't be empty.")
        }*/
        val id = repository.insertFlower(flower)
        return flower.copy(id = id)
    }
}
