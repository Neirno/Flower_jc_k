package com.neirno.flower_jc_k.feature_flower.domain.services

import com.neirno.flower_jc_k.feature_flower.domain.model.Flower

interface AlarmServices {
    fun setAlarm(flower: Flower, actionType: Int)
    fun cancelAlarm(flower: Flower, actionType: Int)
    fun checkAlarm(flower: Flower, actionType: Int): Boolean
}
