package com.neirno.flower_jc_k.feature_flower.presentation.flowers.components

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.neirno.flower_jc_k.R
import com.neirno.flower_jc_k.feature_flower.presentation.components.ButtonType
import com.neirno.flower_jc_k.feature_flower.presentation.flowers.ActiveOperation
import com.neirno.flower_jc_k.feature_flower.presentation.flowers.FlowerViewModel
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class BottomMenuItem(
    val buttonType: ButtonType,
    val operation: ActiveOperation,
)
@Composable
fun BottomMenu(
    modifier: Modifier,
    items: List<BottomMenuItem>,
    onItemClicked: (BottomMenuItem) -> Unit,
    activeOperation: ActiveOperation,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach { item ->
                val isItemActive = item.operation == activeOperation
                println(activeOperation)
                val animatedPadding by animateDpAsState(
                    targetValue = if (isItemActive) 0.dp else 10.dp,
                    animationSpec = /*tween(50, easing = LinearEasing),*/spring(stiffness = Spring.StiffnessLow),
                    label = ""
                )
                Icon(
                    imageVector = if (isItemActive) ButtonType.ACCEPT.image else item.buttonType.image,
                    contentDescription = item.buttonType.description,
                    tint = if (isItemActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .clickable (
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onItemClicked(item)
                        }
                        .size(50.dp)
                        .padding(top = animatedPadding, bottom = animatedPadding)
                )
            }
        }
    }
}

/*@Composable
fun BottomMenuItemView(
    item: BottomMenuItem,
    isActive: Boolean,
    onItemClicked: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Log.i("1", "icon1")

        Icon(
            painter = painterResource(id = item.buttonType.image),
            contentDescription = item.buttonType.description,
            tint = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.clickable(onClick = onItemClicked)
        )
        Log.i("1", "icon2")

    }
}*/
/*

@Composable
fun BottomMenu(
        modifier: Modifier,
        items: List<BottomMenuItem>,
        onItemClicked: (BottomMenuItem) -> Unit,
        activeOperation: ActiveOperation) {
    Log.i("1", "init")
    Log.i("1", "active")


    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Log.i("1", "foreach")

        items.forEach { item ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Log.i("1", "icon1")
                val isActive = item.operation == activeOperation
                Icon(
                    painter = painterResource(id = item.buttonType.image),
                    contentDescription = item.buttonType.description,
                    tint = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable(onClick = { onItemClicked(item) })
                )
                Log.i("1", "icon2")

            }
        }

    }
}
*/
//@Composable
/*
fun BottomMenu(
    modifier: Modifier,
    items: List<BottomMenuItem>,
    onItemClicked: (BottomMenuItem) -> Unit,
    activeOperation: ActiveOperation
) {

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val firstItem = items.first()
            val isFirstActive = firstItem.operation == activeOperation
            Icon(
                painter = painterResource(id = firstItem.buttonType.image),
                contentDescription = firstItem.buttonType.description,
                tint = if (isFirstActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.clickable(onClick = { onItemClicked(firstItem) }).padding(top = 10.dp).size(35.dp)
            )

            Box(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    items.drop(1).dropLast(1).forEach { item ->
                        val isItemActive = item.operation == activeOperation
                        val animatedPadding by animateDpAsState(
                            targetValue = if (isItemActive) 0.dp else 10.dp,
                            animationSpec = tween(durationMillis = 500), label = ""
                        )
                        Icon(
                            painter = if (isItemActive) painterResource(id = R.drawable.ic_home) else painterResource(id = item.buttonType.image),
                            contentDescription = item.buttonType.description,
                            tint = if (isItemActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.clickable(onClick = { onItemClicked(item) }).size(35.dp).padding(top = animatedPadding, bottom = animatedPadding)

                        )
                    }
                }
                Divider(color = Color.White, modifier = Modifier.align(Alignment.BottomCenter).padding(end = 20.dp, start = 20.dp))
            }

            val lastItem = items.last()
            val isLastActive = lastItem.operation == activeOperation
            Icon(
                painter = painterResource(id = lastItem.buttonType.image),
                contentDescription = lastItem.buttonType.description,
                tint = if (isLastActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.clickable(onClick = { onItemClicked(lastItem) }).padding(top = 10.dp).size(35.dp)
            )
        }
    }
}*/
