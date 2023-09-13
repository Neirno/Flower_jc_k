package com.neirno.flower_jc_k.di


import android.content.Context
import androidx.annotation.StringRes
import androidx.room.Room
import com.neirno.flower_jc_k.feature_flower.data.data_source.FlowerDatabase
import com.neirno.flower_jc_k.feature_flower.data.repository.FlowerRepositoryImpl
import com.neirno.flower_jc_k.feature_flower.data.services.AlarmServicesImpl
import com.neirno.flower_jc_k.feature_flower.data.storage.ImageStorageManager
import com.neirno.flower_jc_k.feature_flower.data.storage.ImageStorageManagerImpl
import com.neirno.flower_jc_k.feature_flower.domain.repository.FlowerRepository
import com.neirno.flower_jc_k.feature_flower.domain.services.AlarmServices
import com.neirno.flower_jc_k.feature_flower.domain.use_case.AddFlower
import com.neirno.flower_jc_k.feature_flower.domain.use_case.CancelAlarmForFlower
import com.neirno.flower_jc_k.feature_flower.domain.use_case.CheckAlarmForFlower
import com.neirno.flower_jc_k.feature_flower.domain.use_case.DeleteFlower
import com.neirno.flower_jc_k.feature_flower.domain.use_case.DeleteImage
import com.neirno.flower_jc_k.feature_flower.domain.use_case.FlowerUseCases
import com.neirno.flower_jc_k.feature_flower.domain.use_case.GetFlower

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.neirno.flower_jc_k.feature_flower.domain.use_case.GetFlowers
import com.neirno.flower_jc_k.feature_flower.domain.use_case.SaveImage
import com.neirno.flower_jc_k.feature_flower.domain.use_case.SetAlarmForFlower
import com.neirno.flower_jc_k.feature_flower.domain.use_case.UpdateFertilizingDate
import com.neirno.flower_jc_k.feature_flower.domain.use_case.UpdateFlower
import com.neirno.flower_jc_k.feature_flower.domain.use_case.UpdateSprayingDate
import com.neirno.flower_jc_k.feature_flower.domain.use_case.UpdateWateringDate
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
object FlowerModule {

    @Singleton
    class ResourceProvider @Inject constructor(
        @ApplicationContext private val context: Context
    ) {
        fun getString(@StringRes resId: Int): String {
            return context.getString(resId)
        }
    }


    @Provides
    @Singleton
    fun provideImageStorageManager(
        @ApplicationContext context: Context
    ): ImageStorageManager = ImageStorageManagerImpl(context)


    @Singleton
    @Provides
    fun provideAlarmServices(
        @ApplicationContext context: Context
    ): AlarmServices = AlarmServicesImpl(context)

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
    fun provideFlowerUseCases(
        repository: FlowerRepository,
        alarmServices: AlarmServices,
        storageManager: ImageStorageManager,
        resourceProvider: ResourceProvider
    ): FlowerUseCases {
        return FlowerUseCases(
            getFlowers = GetFlowers(repository),
            deleteFlower = DeleteFlower(repository),
            addFlower = AddFlower(repository, resourceProvider),
            getFlower = GetFlower(repository),
            updateFlower = UpdateFlower(repository),
            updateFertilizingDate = UpdateFertilizingDate(repository),
            updateSprayingDate = UpdateSprayingDate(repository),
            updateWateringDate = UpdateWateringDate(repository),
            setAlarmForFlower = SetAlarmForFlower(alarmServices),
            cancelAlarmForFlower = CancelAlarmForFlower(alarmServices),
            checkAlarmForFlower = CheckAlarmForFlower(alarmServices),
            saveImage = SaveImage(storageManager),
            deleteImage = DeleteImage(storageManager)
        )
    }
}