package com.neirno.flower_jc_k.feature_flower.data.repository

import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import com.neirno.flower_jc_k.feature_flower.data.data_source.FlowerDao
import com.neirno.flower_jc_k.feature_flower.domain.repository.FlowerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar

class FlowerRepositoryImpl (
    private val flowerDao: FlowerDao
) : FlowerRepository {

    override fun getFlowers(): Flow<List<Flower>> {
        return flowerDao.getFlowers()
    }

    override suspend fun getFlowerById(id: Long): Flower? {
        return flowerDao.getFlowerById(id)
    }

    override suspend fun insertFlower(flower: Flower): Long {
        return flowerDao.insert(flower)
    }

    override suspend fun updateFlower(flower: Flower) {
        flowerDao.update(flower)
    }

    override suspend fun deleteFlower(flower: Flower) {
        flowerDao.delete(flower)
    }
    // TODO("Сделать реализацию update для другой модели.")
    override suspend fun updateWateringDate(flower: Flower) {
        val nextWateringDateMillis = getNextActionTimeInMillis(
            flower.wateringDays,
            flower.wateringHours,
            flower.wateringMinutes
        )
        val updatedFlower = flower.copy(nextWateringDateTime = nextWateringDateMillis)
        return flowerDao.update(updatedFlower)

    }

    override suspend fun updateFertilizingDate(flower: Flower) {
        val nextFertilizingDateMillis = getNextActionTimeInMillis(
            flower.fertilizingDays,
            flower.fertilizingHours,
            flower.fertilizingMinutes
        )
        val updatedFlower = flower.copy(nextFertilizingDateTime = nextFertilizingDateMillis)
        flowerDao.update(updatedFlower)
    }

    override suspend fun updateSprayingDate(flower: Flower) {
        val nextSprayingDateMillis = getNextActionTimeInMillis(
            flower.sprayingDays,
            flower.sprayingHours,
            flower.sprayingMinutes
        )
        val updatedFlower = flower.copy(nextSprayingDateTime = nextSprayingDateMillis)
        flowerDao.update(updatedFlower)
    }

    private suspend fun getNextActionTimeInMillis(days: Int, hours: Int, minutes: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, days)
        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, minutes)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

}
