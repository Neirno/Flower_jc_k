package com.neirno.flower_jc_k.feature_flower.background

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.neirno.flower_jc_k.R

class AlarmReceiver : BroadcastReceiver() {
    private fun generateRequestCode(flowerId: Long, actionType: Int): Int {
        return ("${flowerId}00${actionType}").toInt()
    }


    override fun onReceive(context: Context, intent: Intent) {
        Log.i("Work notif","Create Not")

        val flowerId = intent.getLongExtra("flowerId", 0)

        //val channel_id = generateChannelId(actionType, flowerId)
        val actionType = intent.getIntExtra("actionType", 0)
        val actionName = when(actionType) {
            1 -> "Время поливать цветок!"
            2 -> "Время удобрить цветок!"
            3 -> "Время опрыскать цветок!"
            else -> "Что-то пошло не так!"
        }
        val flowerName = intent.getStringExtra("flowerName")
        val notificationId = generateRequestCode(flowerId, actionType)

        val notification = NotificationCompat.Builder(context, "simple_notification_channel")
            .setSmallIcon(R.drawable.ic_flower_list) // Replace with your own icon
            .setContentTitle("Flower Reminder")
            .setContentText("It's time to $actionName your flower (ID: $flowerName)")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            //.setGroup(flowerId.toString())
            //.setGroupSummary(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        // Надеюсь никогда пользователь не зайдет за intы
        notificationManager.notify(notificationId, notification)
        Log.i("Work notif", "All Work")
    }

    private fun createNotificationChannel(context: Context, channel_id: String) {
        val name = "Flower Reminders"
        val descriptionText = "Channel for flower care reminders"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channel_id, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

