package com.neirno.flower_jc_k.feature_flower.presentation.util

sealed class Screen(val route: String) {
    object FlowersScreen: Screen("flowers_screen")
    object AddEditFlowerScreen: Screen("add_edit_flower_screen")
    object CameraScreen: Screen("camera_screen")
}