package com.neirno.flower_jc_k.feature_flower.data.utils

fun generateRequestCode(flowerId: Long, actionType: Int): Int {
    return ("${flowerId}00${actionType}").toInt()
}