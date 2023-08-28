package com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower

import android.net.Uri

data class AddEditFlowerViewState(
    val flowerName: FlowerTextFieldState = FlowerTextFieldState(hint = "Enter name..."),
    val flowerDescription: FlowerTextFieldState = FlowerTextFieldState(hint = "Enter description..."),
    val flowerImageUri: Uri? = null,

    val flowerTimeToWater: Time = Time(0, 0, 0),
    val flowerTimeToSpraying: Time = Time(0, 0, 0),
    val flowerTimeToFertilize: Time = Time(0, 0, 0)
)

data class Time(
    val days: Int,
    val hours: Int,
    val minutes: Int
)


data class FlowerTextFieldState (
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)