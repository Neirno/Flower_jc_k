package com.neirno.flower_jc_k.feature_flower.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.neirno.flower_jc_k.R
import java.time.LocalDateTime

@Entity
data class Flower(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?,
    val imageFilePath: String?, // для изображений, сделанных пользователем
    val wateringFrequency: Int,
    val wateringTime: Int,
    val fertilizingFrequency: Int,
    val fertilizingTime: Int,
    val sprayingFrequency: Int,
    val spayingTime: Int,
    val nextWateringDateTime: Long = System.currentTimeMillis(),
    val nextFertilizingDateTime: Long = System.currentTimeMillis(),
    val nextSprayingDateTime: Long = System.currentTimeMillis(),
) {
    companion object {
        val imageRes = listOf(R.drawable.ic_flower_list, R.drawable.ic_home)
    }
}

class InvalidFlowerException(message: String): Exception(message)