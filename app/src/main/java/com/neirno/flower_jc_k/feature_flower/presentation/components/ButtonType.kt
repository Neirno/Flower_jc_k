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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.neirno.flower_jc_k.R

enum class ButtonType(val image: ImageVector, val description: String) {
    CHAT(Icons.Default.ChatBubble, "Chat"),
    CLOSE(Icons.Default.Close, "Close"),
    BACK(Icons.Default.ArrowBack, "Back"),
    SETTINGS(Icons.Default.Settings, "Settings"),
    WATER(Icons.Default.WaterDrop, "Water"),
    SPRAY(Icons.Default.Spa, "Spray"),
    ADD(Icons.Default.Add, "Add"),
    FERTILIZE(Icons.Default.Spa, "Fertilize"),
    DELETE(Icons.Default.Delete, "Delete"),
    ACCEPT(Icons.Default.Check, "Accept"),
    FLASH_ON(Icons.Default.FlashOn, "Flash on"),
    FLASH_OFF(Icons.Default.FlashOff, "Flash off"),
    NOTHING(Icons.Default.Error, "Nothing")
}
