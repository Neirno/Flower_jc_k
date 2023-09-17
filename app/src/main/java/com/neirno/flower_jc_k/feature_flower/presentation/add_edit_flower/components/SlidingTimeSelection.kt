package com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neirno.flower_jc_k.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.LinkedList
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlidingTimeSelection(
    modifier: Modifier = Modifier,
    text: String = "",
    minValue: Int = 0,
    maxValue: Int = 60,
    defaultValue: Int? = null,
    callBack: (value: Int) -> Unit = {}
) {
    val TAG = "tzmax"

    val itemsList = (minValue..maxValue).toList()
    val itemCount = itemsList.size

    // Вычисляем начальный индекс так, чтобы 0 был по центру
    //val initialIndex = (Int.MAX_VALUE / 2) - (Int.MAX_VALUE / 2) % itemCount -1
    val initialIndex = if (defaultValue != null && itemsList.contains(defaultValue)) {
        (Int.MAX_VALUE / 2) - (Int.MAX_VALUE / 2) % itemCount + itemsList.indexOf(defaultValue) -1
    } else {
        (Int.MAX_VALUE / 2) - (Int.MAX_VALUE / 2) % itemCount -1
    }

    val listState = rememberLazyListState(
        initialIndex
    )
    val coroutineScope = rememberCoroutineScope()

    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    DisposableEffect(Unit) {
        coroutineScope.launch {
            listState.scrollToItem(initialIndex)
        }
        onDispose { }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = snapBehavior,
        ) {
            items(
                count = Int.MAX_VALUE,
                itemContent = { virtualIndex ->
                    val actualIndex = virtualIndex % itemCount
                    val item = itemsList[actualIndex]

                    val centralPosition = remember { derivedStateOf { listState.firstVisibleItemIndex + 1 } }.value
                    val isSelected = virtualIndex == centralPosition

                    if (isSelected) {
                        callBack(item)
                        Log.d(TAG, "SlidingTimeSelection() selected item: $item at virtualIndex: $virtualIndex")
                    }

                    Box(
                        modifier = Modifier
                            .height(25.dp)
                            .then(
                                if (isSelected) {
                                    Modifier.border(
                                        width = 1.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                } else Modifier
                            )  // Добавляем рамку для центрального элемента
                            .padding(horizontal = 4.dp)  // Добавляем небольшой отступ слева и справа для красоты
                    ) {
                        Text(
                            text = if (item > 9) item.toString() else "0$item",
                            textAlign = TextAlign.Center,
                            color = if (isSelected) Color(0xFF0000000) else Color(0x660000000),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.W300,
                            fontSize = if (isSelected) 25.sp else 20.sp
                        )
                    }
                }
            )
        }
        Text(text = text, modifier = Modifier.padding(start = 5.dp))
    }
}
