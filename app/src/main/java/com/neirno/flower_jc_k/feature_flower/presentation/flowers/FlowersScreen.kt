package com.neirno.flower_jc_k.feature_flower.presentation.flowers
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.neirno.flower_jc_k.R
import com.neirno.flower_jc_k.feature_flower.presentation.components.ButtonType
import com.neirno.flower_jc_k.feature_flower.presentation.flowers.components.BottomMenu
import com.neirno.flower_jc_k.feature_flower.presentation.flowers.components.ConfirmationDialog
import com.neirno.flower_jc_k.feature_flower.presentation.flowers.components.FlowerItem
import com.neirno.flower_jc_k.feature_flower.presentation.util.Screen
import com.neirno.flower_jc_k.feature_flower.presentation.flowers.components.BottomMenuItem
import com.neirno.flower_jc_k.feature_flower.presentation.flowers.components.FlowerOrderDropdown
import com.neirno.flower_jc_k.ui.theme.CustomBlue
import com.neirno.flower_jc_k.ui.theme.CustomBrown
import com.neirno.flower_jc_k.ui.theme.CustomDark
import com.neirno.flower_jc_k.ui.theme.CustomGreen
import com.neirno.flower_jc_k.ui.theme.CustomWhite

@Composable
fun FlowersScreen(
    navController: NavController,
    viewState: FlowerViewState,
    onEvent: (FlowersEvent) -> Unit
) {
    val bottomMenuItems = listOf(
        BottomMenuItem(ButtonType.ADD, ActiveOperation.ADD, CustomDark),
        BottomMenuItem(ButtonType.SPRAYING, ActiveOperation.SPRAY, CustomGreen),
        BottomMenuItem(ButtonType.WATER, ActiveOperation.WATER, CustomBlue),
        BottomMenuItem(ButtonType.FERTILIZE, ActiveOperation.FERTILIZE, Color.Yellow),
        BottomMenuItem(ButtonType.DELETE, ActiveOperation.DELETE, CustomDark),
    )

    BackHandler(viewState.activeOperation != ActiveOperation.NONE) {
        onEvent(FlowersEvent.ClearSelectedFlower)
        onEvent(FlowersEvent.SetActiveOperation(ActiveOperation.NONE))
    }
    Scaffold(
        containerColor = CustomWhite,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)
                    .background(color = CustomWhite),


                horizontalArrangement = Arrangement.Start,
                verticalAlignment = CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Spacer(modifier = Modifier.fillMaxSize())
                    FlowerOrderDropdown(
                        flowerOrder = viewState.orderState.flowerOrder,
                        onOrderChange = { order -> onEvent(FlowersEvent.Order(order)) },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 8.dp)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.romashka),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                )
            }
        },
        bottomBar = {
            BottomMenu(
                modifier = Modifier,
                items = bottomMenuItems,
                onItemClicked = { menuItem ->
                    val isActive = viewState.activeOperation == menuItem.operation
                    val noFlowersSelected = viewState.selectedFlowers.isEmpty()
                    when (menuItem.operation) {
                        ActiveOperation.ADD -> {
                            navController.navigate(Screen.AddEditFlowerScreen.route)
                        }

                        ActiveOperation.SPRAY, ActiveOperation.WATER, ActiveOperation.FERTILIZE, ActiveOperation.DELETE -> {
                            when {
                                !isActive && viewState.selectedFlowers.isNotEmpty() -> {
                                    onEvent(FlowersEvent.ClearSelectedFlower)
                                    onEvent(FlowersEvent.SetActiveOperation(menuItem.operation))
                                }

                                isActive && noFlowersSelected -> {
                                    onEvent(
                                        FlowersEvent.SetActiveOperation(
                                            ActiveOperation.NONE
                                        )
                                    )
                                }

                                isActive && !noFlowersSelected -> {
                                    handleConfirmation(menuItem.operation, onEvent, viewState)
                                    onEvent(
                                        FlowersEvent.SetActiveOperation(
                                            ActiveOperation.NONE
                                        )
                                    )
                                }

                                else -> {
                                    onEvent(FlowersEvent.SetActiveOperation(menuItem.operation))
                                }
                            }
                        }
                        // Другие операции, если нужно
                        else -> { /* ... */
                        }
                    }
                },
                activeOperation = viewState.activeOperation,
            )
        },
    ) { contentPadding ->
        Column (modifier = Modifier.padding(contentPadding)) {

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(viewState.orderState.flowers) { flowerItem ->
                    Log.i(flowerItem.name, flowerItem.nextWateringDateTime.toString())
                    FlowerItem(
                        flower = flowerItem,
                        onItemSelected = {
                            if (viewState.activeOperation != ActiveOperation.NONE) {
                                onEvent(FlowersEvent.SelectFlower(flowerItem))
                            } else {
                                navController.navigate(
                                Screen.AddEditFlowerScreen.route + "?flowerId=${flowerItem.id}"
                                )
                            }
                        },
                        isSelectionMode = viewState.activeOperation != ActiveOperation.NONE,
                        isSelected = flowerItem in viewState.selectedFlowers
                    )
                    Divider()
                }
            }
        }
    }

    ConfirmationDialog(
        showDialog = viewState.showConfirmationDialog,
        title = stringResource(id = R.string.delete),
        description = stringResource(id = R.string.delete_confirm),
        onHide = {
            onEvent(FlowersEvent.SetActiveOperation(ActiveOperation.NONE))
            onEvent(FlowersEvent.ClearSelectedFlower)
            onEvent(FlowersEvent.HideConfirmationDialog)
        },
        onConfirm = {
            onEvent(FlowersEvent.DeleteFlower(viewState.selectedFlowers))
            onEvent(FlowersEvent.SetActiveOperation(ActiveOperation.NONE))
            onEvent(FlowersEvent.ClearSelectedFlower)
        }
    )
}

private fun handleConfirmation(
    operation: ActiveOperation,
    onEvent: (FlowersEvent) -> Unit,
    viewState: FlowerViewState
) {
    when (operation) {
        ActiveOperation.FERTILIZE -> {
            onEvent(FlowersEvent.UpdateFertilize(viewState.selectedFlowers))
            onEvent(FlowersEvent.ClearSelectedFlower)
        }

        ActiveOperation.WATER -> {
            onEvent(FlowersEvent.UpdateWater(viewState.selectedFlowers))
            onEvent(FlowersEvent.ClearSelectedFlower)
        }
        ActiveOperation.SPRAY -> {
            onEvent(FlowersEvent.UpdateSpray(viewState.selectedFlowers))
            onEvent(FlowersEvent.ClearSelectedFlower)
        }
        ActiveOperation.DELETE -> onEvent(FlowersEvent.ShowConfirmationDialog)
        else -> { /* default behavior */ }
    }
}
