package com.neirno.flower_jc_k.feature_flower.background

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import java.util.concurrent.TimeUnit

private fun generateRequestCode(flowerId: Long, actionType: Int): Int {
    return (flowerId.toString() + actionType.toString()).hashCode()
}



fun setAlarm(context: Context, flower: Flower, actionType: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("flowerId", flower.id)
        putExtra("flowerName", flower.name)
        putExtra("actionType", actionType)
    }

    val requestCode = generateRequestCode(flower.id, actionType)
    val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

    val nextAlarmDateTime = when (actionType) {
        1 -> flower.nextWateringDateTime + TimeUnit.DAYS.toMillis(flower.wateringDays.toLong())
        2 -> flower.nextFertilizingDateTime + TimeUnit.DAYS.toMillis(flower.fertilizingDays.toLong())
        3 -> flower.nextSprayingDateTime + TimeUnit.DAYS.toMillis(flower.sprayingDays.toLong())
        else -> throw IllegalArgumentException("Invalid action type")
    }

    alarmManager.setExactAndAllowWhileIdle (
        AlarmManager.RTC_WAKEUP,
        nextAlarmDateTime,
        pendingIntent
    )
    if (pendingIntent != null) {
        val alarmClockInfos = alarmManager.nextAlarmClock
        Log.i("Alarm!!!@@", "Alarm is set to2: $nextAlarmDateTime")
    } else {
        Log.i("Alarm", "No alarm is set.")
    }
}

fun checkAlarm(context: Context, flower: Flower, actionType: Int): Boolean {
    val intent = Intent(context, AlarmReceiver::class.java)
    val requestCode = generateRequestCode(flower.id, actionType)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
    )

    return pendingIntent != null
}

fun cancelAlarm(context: Context, flowerId: Long, actionType: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val requestCode = generateRequestCode(flowerId, actionType)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
    )

    if (pendingIntent != null) {
        alarmManager.cancel(pendingIntent)
    }
}
