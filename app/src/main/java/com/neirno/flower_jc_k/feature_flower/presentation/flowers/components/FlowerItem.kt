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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds

//@SuppressLint("CoroutineCreationDuringComposition")
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
            error(R.drawable.standart_flower) // предоставьте свое изображение-заглушку
            fallback(R.drawable.standart_flower) // предоставьте свое изображение-заглушку
        }
    )
    val daysUntilNextFertilizing = TimeUnit.MILLISECONDS.toDays(flower.nextFertilizingDateTime - System.currentTimeMillis()).coerceAtLeast(0)
    var daysUntilNextWatering by remember { mutableStateOf(  millisToDdHhMm(TimeUnit.MILLISECONDS.toDays(flower.nextWateringDateTime - System.currentTimeMillis()).coerceAtLeast(0)))}
    Log.d("Next watering days in {${flower.name}", daysUntilNextWatering)
    /*val scope = rememberCoroutineScope()
    scope.launch {
        while (true) {
            daysUntilNextWatering = millisToDdHhMm(TimeUnit.MILLISECONDS.toMillis(flower.nextWateringDateTime - System.currentTimeMillis()).coerceAtLeast(0))
            delay(1000)  // перерисовать каждую секунду
        }
    }*/
    LaunchedEffect(key1 = flower) {
        while (true) {
            daysUntilNextWatering = millisToDdHhMm(TimeUnit.MILLISECONDS.toMillis(flower.nextWateringDateTime - System.currentTimeMillis()).coerceAtLeast(0))
            delay(1000)  // перерисовать каждую секунду
        }
    }

    //val daysUntilNextWatering = millisToDdHhMm(TimeUnit.MILLISECONDS.toMillis(flower.nextWateringDateTime - System.currentTimeMillis()).coerceAtLeast(0))
    // Вычисляем разницу во времени до следующего действия
    val maxTime = TimeUnit.DAYS.toMillis(flower.wateringDays.toLong())
    val elapsedTime = flower.nextWateringDateTime - System.currentTimeMillis()
    val wateringProgress = ((maxTime - elapsedTime).toFloat() / maxTime.toFloat()).coerceAtLeast(0F)

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
                    .size(50.dp)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 4.dp, bottomEnd = 4.dp)),
                contentScale = ContentScale.Crop,

            )
            if (isSelectionMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onItemSelected(flower) },
                    modifier = Modifier.align(Alignment.TopStart) // чтобы чекбокс появлялся в верхнем левом углу изображения
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            Text(
                text = flower.name, style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
           /* LinearProgressIndicator(
                progress = 1f - (daysUntilNextWatering.toFloat() / 7f),
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )*/


            Text(
                text = "Дней до полива: $daysUntilNextWatering",
                style = MaterialTheme.typography.labelSmall/*bodyMedium*/
            )
            LinearProgressIndicator(
                progress = /*1f - */wateringProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black)
                    .clip(RoundedCornerShape(8.dp)), // Закругленные углы с радиусом 8.dp
                color = Color.Gray,
                trackColor = Color.Blue
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Дней до удобрения: $daysUntilNextFertilizing",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}


private fun millisToDdHhMm(millis: Long): String {
    val days = TimeUnit.MILLISECONDS.toDays(millis)
    val hours = TimeUnit.MILLISECONDS.toHours(millis) % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    return String.format("%02d:%02d:%02d", days, hours, minutes)
}
