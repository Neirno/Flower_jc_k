package com.neirno.flower_jc_k.feature_flower.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import com.neirno.flower_jc_k.R

enum class ButtonType(val imageProvider: @Composable () -> Painter, val description: String) {
    CHAT({ Icons.Default.ChatBubble.asPainter() }, "Chat"),
    CLOSE({ Icons.Default.Close.asPainter() }, "Close"),
    BACK({ Icons.Default.ArrowBack.asPainter() }, "Back"),
    SETTINGS({ Icons.Default.Settings.asPainter() }, "Settings"),
    WATER({ Icons.Default.WaterDrop.asPainter() }, "Water"),
    SPRAYING({ painterResource(id = R.drawable.spraying)}, "Spray"),
    ADD({ Icons.Default.Add.asPainter() }, "Add"),
    FERTILIZE({ painterResource(id = R.drawable.fertilize)} , "Fertilize"),
    DELETE({ Icons.Default.Delete.asPainter() }, "Delete"),
    ACCEPT({ Icons.Default.Check.asPainter() }, "Accept"),
    FLASH_ON({ Icons.Default.FlashOn.asPainter() }, "Flash on"),
    FLASH_OFF({ Icons.Default.FlashOff.asPainter() }, "Flash off"),
    NOTHING({ Icons.Default.Error.asPainter() }, "Nothing"),
    ORDER({ Icons.Default.List.asPainter() }, "Menu list"),
}

@Composable
fun ImageVector.asPainter(): Painter {
    return rememberVectorPainter(this)
}
