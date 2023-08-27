package com.neirno.flower_jc_k.feature_flower.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.neirno.flower_jc_k.feature_flower.domain.model.Flower

@Database(
    entities = [Flower::class],
    version = 1
)
abstract class FlowerDatabase: RoomDatabase() {

    abstract val flowerDao: FlowerDao

    companion object {
        const val DATABASE_NAME = "flower_db"
    }
}