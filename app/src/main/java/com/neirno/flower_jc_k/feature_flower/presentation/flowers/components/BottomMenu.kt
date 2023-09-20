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
                val animatedPadding by animateDpAsState(
                    targetValue = if (isItemActive) 0.dp else 10.dp,
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
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
