package com.neirno.flower_jc_k.feature_flower.presentation.flowers.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.neirno.flower_jc_k.R
import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.neirno.flower_jc_k.feature_flower.presentation.components.ButtonType
import com.neirno.flower_jc_k.ui.theme.CustomBlue
import com.neirno.flower_jc_k.ui.theme.CustomDark
import com.neirno.flower_jc_k.ui.theme.CustomGreen
import com.neirno.flower_jc_k.ui.theme.CustomWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun FlowerItem(
    flower: Flower,
    modifier: Modifier = Modifier,
    onItemSelected: (Flower) -> Unit,
    isSelectionMode: Boolean,
    isSelected: Boolean
) {

    // Загрузка изображения из ресурсов или из файловой системы
    val painter = rememberImagePainter(
        data = "file:///${flower.imageFilePath}",
        builder = {
            crossfade(true)
            error(R.drawable.standart_flower)
            fallback(R.drawable.standart_flower)
        }
    )
    val daysUntilNextFertilizing = TimeUnit.MILLISECONDS.toDays(flower.nextFertilizingDateTime - System.currentTimeMillis()).coerceAtLeast(0)
    var daysUntilNextWatering by remember { mutableStateOf(  millisToDdHhMm(TimeUnit.MILLISECONDS.toDays(flower.nextWateringDateTime - System.currentTimeMillis()).coerceAtLeast(0)))}
    var daysUntilNextSpraying by remember { mutableStateOf(  millisToDdHhMm(TimeUnit.MILLISECONDS.toDays(flower.nextSprayingDateTime - System.currentTimeMillis()).coerceAtLeast(0)))}

    LaunchedEffect(key1 = flower) {
        while (true) {
            daysUntilNextWatering = millisToDdHhMm(TimeUnit.MILLISECONDS.toMillis(flower.nextWateringDateTime - System.currentTimeMillis()).coerceAtLeast(0))
            daysUntilNextSpraying = millisToDdHhMm(TimeUnit.MILLISECONDS.toMillis(flower.nextSprayingDateTime - System.currentTimeMillis()).coerceAtLeast(0))
            delay(10000)
        }
    }

    // Вычисляем разницу во времени до следующего полива
    val maxTimeWater = TimeUnit.DAYS.toMillis(flower.wateringDays.toLong())
    val elapsedTimeWater = flower.nextWateringDateTime - System.currentTimeMillis()
    val wateringProgressWater = ((maxTimeWater - elapsedTimeWater).toFloat() / maxTimeWater.toFloat()).coerceAtLeast(0F)


    // Вычисляем разницу во времени до следующего опрыскивания
    val maxTimeSpray = TimeUnit.DAYS.toMillis(flower.wateringDays.toLong())
    val elapsedTimeSpray = flower.nextSprayingDateTime - System.currentTimeMillis()
    val wateringProgressSpray = ((maxTimeSpray - elapsedTimeSpray).toFloat() / maxTimeSpray.toFloat()).coerceAtLeast(0F)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemSelected(flower) },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            contentAlignment = Alignment.CenterStart
        ) {
            Image(
                painter = painter,
                contentDescription = "${flower.name} image",
                modifier = Modifier
                    .size(75.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 4.dp,
                            bottomStart = 4.dp,
                            bottomEnd = 4.dp
                        )
                    ),
                contentScale = ContentScale.Crop,

            )
            if (isSelectionMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onItemSelected(flower) },
                    modifier = Modifier.align(Alignment.Center),
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.White,   // цвет при выбранном состоянии
                        uncheckedColor = Color.White,  // цвет при невыбранном состоянии
                        checkmarkColor = CustomDark, // цвет галочки
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = flower.name, style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (flower.wateringDays != 0) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        tint = CustomBlue,
                        painter = ButtonType.WATER.imageProvider(),
                        contentDescription = ButtonType.WATER.description
                    )
                    Column {
                        Text(
                            text = "${stringResource(id = R.string.water)}: $daysUntilNextWatering",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = /*1f - */wateringProgressWater,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .border(1.dp, Color.Black)
                                .clip(RoundedCornerShape(8.dp)),
                            color = CustomDark,
                            trackColor = CustomBlue
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (flower.sprayingDays != 0) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        tint = CustomGreen,
                        painter = ButtonType.SPRAYING.imageProvider(),
                        contentDescription = ButtonType.SPRAYING.description
                    )
                    Column {
                        Text(
                            text = "${stringResource(id = R.string.spraying)}: $daysUntilNextSpraying",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = wateringProgressSpray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .border(1.dp, Color.Black)
                                .clip(RoundedCornerShape(8.dp)),
                            color = CustomDark,
                            trackColor = CustomGreen
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (flower.fertilizingDays != 0) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        tint = Color.Yellow,
                        painter = ButtonType.FERTILIZE.imageProvider(),
                        contentDescription = ButtonType.FERTILIZE.description
                    )
                    Text(
                        text = "${stringResource(id = R.string.fertilize)}: $daysUntilNextFertilizing дней",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}


private fun millisToDdHhMm(millis: Long): String {
    val days = TimeUnit.MILLISECONDS.toDays(millis)
    val hours = TimeUnit.MILLISECONDS.toHours(millis) % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    return String.format("%02d:%02d:%02d", days, hours, minutes)
}
