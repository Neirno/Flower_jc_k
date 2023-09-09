package com.neirno.flower_jc_k.feature_flower.data.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.neirno.flower_jc_k.feature_flower.data.receivers.AlarmReceiver
import com.neirno.flower_jc_k.feature_flower.data.utils.generateRequestCode
import com.neirno.flower_jc_k.feature_flower.domain.model.Flower
import com.neirno.flower_jc_k.feature_flower.domain.services.AlarmServices

class AlarmServicesImpl(
    private val context: Context
) : AlarmServices {
    override fun setAlarm(flower: Flower, actionType: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("flowerId", flower.id)
            putExtra("flowerName", flower.name)
            putExtra("actionType", actionType)
        }

        val requestCode = generateRequestCode(flower.id, actionType)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val nextAlarmDateTime = when (actionType) {
            1 -> flower.nextWateringDateTime
            2 -> flower.nextFertilizingDateTime
            3 -> flower.nextSprayingDateTime
            else -> throw IllegalArgumentException("Invalid action type")
        }

        alarmManager.setExactAndAllowWhileIdle (
            AlarmManager.RTC_WAKEUP,
            nextAlarmDateTime,
            pendingIntent
        )
        if (pendingIntent != null) {
            Log.i("Alarm!!!@@", "Alarm is set to2: $nextAlarmDateTime")
        } else {
            Log.i("Alarm", "No alarm is set.")
        }
    }

    override fun cancelAlarm(flower: Flower, actionType: Int) {
        val flowerId = flower.id
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = generateRequestCode(
            flowerId,
            actionType
        )
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

    override fun checkAlarm(flower: Flower, actionType: Int): Boolean {
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = generateRequestCode(
            flower.id,
            actionType
        )
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        return pendingIntent != null
    }
}
