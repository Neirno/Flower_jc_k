package com.neirno.flower_jc_k.feature_flower.domain.use_case

import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import com.neirno.flower_jc_k.feature_flower.domain.services.AlarmServices

class CancelAlarmForFlower(
    private val alarmServices: AlarmServices
) {
    suspend operator fun invoke(flower: Flower, actionType: Int) {
        return alarmServices.cancelAlarm(flower, actionType)
    }
}
