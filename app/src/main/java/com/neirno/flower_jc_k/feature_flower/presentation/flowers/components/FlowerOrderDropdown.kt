package com.neirno.flower_jc_k.feature_flower.presentation.flowers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import com.neirno.flower_jc_k.R
import com.neirno.flower_jc_k.feature_flower.domain.util.FlowerOrder
import com.neirno.flower_jc_k.feature_flower.domain.util.OrderType


@Composable
fun FlowerOrderDropdown(
    flowerOrder: FlowerOrder = FlowerOrder.Water(OrderType.Descending),
    onOrderChange: (FlowerOrder) -> Unit,
    modifier: Modifier = Modifier
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var buttonCoordinates by remember { mutableStateOf(IntOffset(0, 0)) }


    Column(
        modifier = modifier/*.padding(start = 10.dp)*/,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Ваш Row и все что в нем находится здесь

        IconButton(
            onClick = { isDropdownExpanded = true },
            modifier = Modifier/*.border(1.dp, MaterialTheme.colorScheme.primary)*/
                .onGloballyPositioned { layoutCoordinates ->
                    val position = layoutCoordinates.localToWindow(Offset.Zero)
                    buttonCoordinates = IntOffset(
                        x = (position.x + layoutCoordinates.size.width).toInt()-50,
                        y = (position.y + layoutCoordinates.size.height).toInt()-100
                    )
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_help),
                contentDescription = "Description",
                tint = Color(0xFF079B6D)
            )
        }
    }

    if (isDropdownExpanded) {
        Popup(onDismissRequest = { isDropdownExpanded = false }, offset = /*IntOffset(96, 72)*/buttonCoordinates) {
            CustomDropdownMenu(
                modifier = Modifier,
                expanded = isDropdownExpanded,
            ) {
                DefaultRadioButton(
                    text = "Name",
                    selected = flowerOrder is FlowerOrder.Name,
                    onSelect = { onOrderChange(FlowerOrder.Name(flowerOrder.orderType)) }
                )
                //Spacer(modifier = Modifier.width(2.dp))
                DefaultRadioButton(
                    text = "Water",
                    selected = flowerOrder is FlowerOrder.Water,
                    onSelect = { onOrderChange(FlowerOrder.Water(flowerOrder.orderType)) }
                )
                //Spacer(modifier = Modifier.width(2.dp))
                DefaultRadioButton(
                    text = "Fertilize",
                    selected = flowerOrder is FlowerOrder.Fertilize,
                    onSelect = { onOrderChange(FlowerOrder.Fertilize(flowerOrder.orderType)) }
                )
                //Spacer(modifier = Modifier.width(2.dp))
                DefaultRadioButton(
                    text = "Spraying",
                    selected = flowerOrder is FlowerOrder.Spraying,
                    onSelect = { onOrderChange(FlowerOrder.Spraying(flowerOrder.orderType)) }
                )

                Divider(modifier = Modifier.width(45.dp))

                DefaultRadioButton(
                    text = "Ascending",
                    selected = flowerOrder.orderType is OrderType.Ascending,
                    onSelect = {
                        onOrderChange(flowerOrder.copy(OrderType.Ascending))
                    }
                )
                //Spacer(modifier = Modifier.width(2.dp))
                DefaultRadioButton(
                    text = "Descending",
                    selected = flowerOrder.orderType is OrderType.Descending,
                    onSelect = {
                        onOrderChange(flowerOrder.copy(OrderType.Descending))
                    }
                )
            }
            // ... Другие элементы
        }
    }
}



@Composable
fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = onSelect)
            .width(200.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            //interactionSource = interactionSource,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun CustomDropdownMenu(
    modifier: Modifier,
    expanded: Boolean,
    //onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    if (expanded) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                //.border(1.dp, MaterialTheme.colorScheme.primary)
                //.clickable { onDismissRequest() }
        ) {
            Column(
                content = content
            )
        }
    }
}
