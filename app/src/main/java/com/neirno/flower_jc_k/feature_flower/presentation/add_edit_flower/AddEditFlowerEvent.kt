package com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower

import android.net.Uri
import androidx.compose.ui.focus.FocusState

sealed class AddEditFlowerEvent {
    data class EnteredName(val value: String) : AddEditFlowerEvent()
    data class ChangeNameFocus(val focusState: FocusState) : AddEditFlowerEvent()
    data class EnteredDescription(val value: String) : AddEditFlowerEvent()
    data class ChangeDescriptionFocus(val focusState: FocusState) : AddEditFlowerEvent()
    data class SetImage(val uri: Uri) : AddEditFlowerEvent()

    data class ChangeDaysToWater(val days: Int) : AddEditFlowerEvent()
    data class ChangeDaysToFertilize(val days: Int) : AddEditFlowerEvent()
    data class ChangeDaysToSpraying(val days: Int) : AddEditFlowerEvent()

    data class ChangeMinutesToWater(val hours: Int, val minutes: Int) : AddEditFlowerEvent()
    data class ChangeMinutesToFertilize(val hours: Int, val minutes: Int) : AddEditFlowerEvent()
    data class ChangeMinutesToSpraying(val hours: Int, val minutes: Int) : AddEditFlowerEvent()

    object SaveFlower : AddEditFlowerEvent()
}
