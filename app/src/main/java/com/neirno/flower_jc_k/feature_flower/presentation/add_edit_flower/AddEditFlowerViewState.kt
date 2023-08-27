package com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower

import android.net.Uri

data class AddEditFlowerViewState(
    val flowerName: FlowerTextFieldState = FlowerTextFieldState(hint = "Enter name..."),
    val flowerDescription: FlowerTextFieldState = FlowerTextFieldState(hint = "Enter description..."),
    val flowerImageUri: Uri? = null,
    val flowerDaysToWater: Int = 0,
    val flowerDaysToFertilize: Int = 0,
    val flowerDaysToSpraying: Int = 0,
    val flowerMinutesToWater: Int = 0,
    val flowerMinutesToFertilize: Int = 0,
    val flowerMinutesToSpraying: Int = 0,

    //val flowerTimeToWater: List<Int> = listOf(0,0)
    //val flowerTimeToSpraying: List<Int> = listOf(0,0)
    //val flowerTimeToFertilize: List<Int> = listOf(0,0)
)
