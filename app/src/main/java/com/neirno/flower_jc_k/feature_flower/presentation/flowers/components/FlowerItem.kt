package com.neirno.flower_jc_k.feature_flower.presentation.flowers.components

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.neirno.flower_jc_k.R
import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import kotlinx.coroutines.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import java.util.concurrent.TimeUnit

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
            error(R.drawable.ic_help) // предоставьте свое изображение-заглушку
            fallback(R.drawable.ic_home) // предоставьте свое изображение-заглушку
        }
    )

    if (flower.imageFilePath != null) {
        Log.i("ImagePath", flower.imageFilePath)
    }


    // Вычисляем количество дней до следующего полива
    val daysUntilNextFertilizing = TimeUnit.MILLISECONDS.toDays(flower.nextFertilizingDateTime - System.currentTimeMillis()).coerceAtLeast(0)
    val daysUntilNextSpraying = TimeUnit.MILLISECONDS.toDays(flower.nextSprayingDateTime - System.currentTimeMillis()).coerceAtLeast(0)
    //val daysUntilNextWatering = TimeUnit.MILLISECONDS.toDays(flower.nextWateringDateTime - System.currentTimeMillis()).coerceAtLeast(0)
    val daysUntilNextWatering = millisToDdHhMm(TimeUnit.MILLISECONDS.toMillis(flower.nextWateringDateTime - System.currentTimeMillis())).coerceAtLeast(
        0.toString()
    )
    val minutesUntilNextWatering = TimeUnit.MILLISECONDS.toMinutes(flower.nextWateringDateTime - System.currentTimeMillis()).coerceAtLeast(0)

    val wateringProgress = if (flower.wateringFrequency != 0) {
        minutesUntilNextWatering.toFloat() / (flower.wateringFrequency*24*60 + flower.wateringTime).toFloat()
    } else {
        0f
    }


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
                contentScale = ContentScale.Crop,
                contentDescription = "${flower.name} image",
                modifier = Modifier.size(50.dp)
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
                progress = 1f - wateringProgress,
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
