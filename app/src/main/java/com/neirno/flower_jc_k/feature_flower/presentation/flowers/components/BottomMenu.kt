package com.neirno.flower_jc_k.feature_flower.presentation.flowers.components

import androidx.compose.animation.core.Spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.neirno.flower_jc_k.feature_flower.presentation.components.ButtonType
import com.neirno.flower_jc_k.feature_flower.presentation.flowers.ActiveOperation
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import com.neirno.flower_jc_k.ui.theme.CustomGreen

data class BottomMenuItem(
    val buttonType: ButtonType,
    val operation: ActiveOperation,
    val color: Color
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
                    painter = if (isItemActive) ButtonType.ACCEPT.imageProvider() else item.buttonType.imageProvider(),
                    contentDescription = item.buttonType.description,
                    tint = if (isItemActive) CustomGreen else item.color,
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
