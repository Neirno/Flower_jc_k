package com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import com.neirno.flower_jc_k.R

@Composable
fun FlowerImageButton(onClick: () -> Unit, imageId: Int) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(100.dp)
    ) {
        Image(
            painter = painterResource(imageId),
            contentDescription = "Flower Image",
            modifier = Modifier
                .size(100.dp)
                .background(Color.Gray, CircleShape)
                .padding(16.dp)
        )
    }
}

@Composable
fun FlowerImagePicker(onImageSelected: (Int) -> Unit) {
    val images = listOf(
        R.drawable.ic_flower_list,
        R.drawable.ic_flower_list,
        R.drawable.ic_flower_list,
        R.drawable.ic_flower_list,
        R.drawable.ic_flower_list,
        R.drawable.ic_flower_list,
        R.drawable.ic_flower_list,
    ) // замените на ваш список изображений

    MaterialTheme(
        colorScheme = darkColorScheme(),
        typography = Typography()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            IconButton(
                onClick = { onImageSelected(R.drawable.ic_help) },
                modifier = Modifier.size(100.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_help),
                    contentDescription = "Camera Image",
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Gray, CircleShape)
                        .padding(16.dp)
                )
            }

            LazyRow {
                items(images) { image ->
                    FlowerImageButton(
                        onClick = { onImageSelected(image) },
                        imageId = image
                    )
                }
            }
        }
    }
}
