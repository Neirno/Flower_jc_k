/*
package com.neirno.flower_jc_k

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.neirno.flower_jc_k.feature_flower.presentation.flowers.components.BottomMenu
import com.neirno.flower_jc_k.feature_flower.presentation.components.CustomTopAppBar
import com.neirno.flower_jc_k.feature_flower.presentation.flowers.FlowerViewModel


@Composable
fun MainPage(navController: NavController,
             flowerViewModel: FlowerViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        CustomTopAppBar(
            flowerViewModel = flowerViewModel,
            onClickB = {
                navController.popBackStack()
            },
            onClickBB = {}
        )
        Log.i("2","Flower")
        Row(modifier = Modifier.weight(1f)) {
            FlowerList(flowerViewModel = flowerViewModel)
        }
        Log.i("2","Bottom")
        BottomMenu(
            navController = navController,
        )
    }
}*/
