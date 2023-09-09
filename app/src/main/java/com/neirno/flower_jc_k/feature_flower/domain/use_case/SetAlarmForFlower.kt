package com.neirno.flower_jc_k.feature_flower.domain.use_case

import android.app.AlarmManager
import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import com.neirno.flower_jc_k.feature_flower.domain.services.AlarmServices

class SetAlarmForFlower(
    private val alarmServices: AlarmServices
) {
    suspend operator fun invoke(flower: Flower, actionType: Int) {
        return alarmServices.setAlarm(flower, actionType)
    }
}
