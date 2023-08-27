package com.neirno.flower_jc_k.di


import android.app.Application
import android.content.Context
import androidx.room.Room
import com.neirno.flower_jc_k.feature_flower.data.data_source.FlowerDatabase
import com.neirno.flower_jc_k.feature_flower.data.repository.FlowerRepositoryImpl
import com.neirno.flower_jc_k.feature_flower.domain.repository.FlowerRepository
import com.neirno.flower_jc_k.feature_flower.domain.use_case.AddFlower
import com.neirno.flower_jc_k.feature_flower.domain.use_case.DeleteFlower
import com.neirno.flower_jc_k.feature_flower.domain.use_case.FlowerUseCases
import com.neirno.flower_jc_k.feature_flower.domain.use_case.GetFlower

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.neirno.flower_jc_k.feature_flower.domain.use_case.GetFlowers
import com.neirno.flower_jc_k.feature_flower.domain.use_case.UpdateFertilizingDate
import com.neirno.flower_jc_k.feature_flower.domain.use_case.UpdateFlower
import com.neirno.flower_jc_k.feature_flower.domain.use_case.UpdateSprayingDate
import com.neirno.flower_jc_k.feature_flower.domain.use_case.UpdateWateringDate

@Module
@InstallIn(SingletonComponent::class)
object FlowerModule {

/*    @Provides
    fun provideFlowerCareSchedule(@ApplicationContext context: Context): FlowerCareSchedule {
        return FlowerCareSchedule(context)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }*/

    @Singleton
    @Provides
    fun provideFlowerRepository(db: FlowerDatabase): FlowerRepository {
        return FlowerRepositoryImpl(db.flowerDao)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): FlowerDatabase {
        return Room.databaseBuilder(
            appContext,
            FlowerDatabase::class.java,
            FlowerDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideFlowerUseCases(repository: FlowerRepository): FlowerUseCases {
        return FlowerUseCases(
            getFlowers = GetFlowers(repository),
            deleteFlower = DeleteFlower(repository),
            addFlower = AddFlower(repository),
            getFlower = GetFlower(repository),
            updateFlower = UpdateFlower(repository),
            updateFertilizingDate = UpdateFertilizingDate(repository),
            updateSprayingDate = UpdateSprayingDate(repository),
            updateWateringDate = UpdateWateringDate(repository),
        )
    }
}