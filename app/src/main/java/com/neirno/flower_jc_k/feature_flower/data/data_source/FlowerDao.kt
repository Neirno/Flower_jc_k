package com.neirno.flower_jc_k.feature_flower.data.data_source

import androidx.room.*
import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import kotlinx.coroutines.flow.Flow

@Dao
interface FlowerDao {
    @Query("SELECT * FROM flower")
    fun getFlowers(): Flow<List<Flower>>

    @Query("SELECT * FROM flower WHERE id = :id")
    suspend fun getFlowerById(id: Int): Flower?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flower: Flower): Long

    @Update
    suspend fun update(flower: Flower)

    @Delete
    suspend fun delete(flower: Flower)

}

