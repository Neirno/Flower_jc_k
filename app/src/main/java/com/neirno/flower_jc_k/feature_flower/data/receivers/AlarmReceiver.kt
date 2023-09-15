package com.neirno.flower_jc_k.feature_flower.data.receivers

import android.Manifest
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.neirno.flower_jc_k.R
import com.neirno.flower_jc_k.feature_flower.data.utils.generateRequestCode
import com.neirno.flower_jc_k.feature_flower.presentation.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.i("Work notif","Create Not")

        val flowerId = intent.getLongExtra("flowerId", 0)
 
        //val channel_id = generateChannelId(actionType, flowerId)
        val actionType = intent.getIntExtra("actionType", 0)
        val actionName = when(actionType) {
            1 -> context.getString(R.string.notif_water)
            2 -> context.getString(R.string.notif_fretiliz)
            3 -> context.getString(R.string.notif_spray)
            else -> context.getString(R.string.error)
        }
        val flowerName = intent.getStringExtra("flowerName")
        val notificationId = generateRequestCode(flowerId, actionType)

        // Create an Intent for the activity you want to start
        val resultIntent = Intent(context, MainActivity::class.java)
        // Create the TaskStackBuilder
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

// Сводное уведомление
        val summaryNotification = NotificationCompat.Builder(context, "flower_helper")
            .setContentTitle("Новые задачи")
            .setContentText("У вас есть новые задачи по уходу за растениями!")
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true) // Уведомление будет автоматически закрыто после нажатия
            .setGroup("flower_helper_group")
            .setGroupSummary(true)
            .build()




        val notification = NotificationCompat.Builder(context, "flower_helper")
            .setSmallIcon(R.drawable.ic_notification) // Replace with your own icon
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("$actionName $flowerName!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(resultPendingIntent) // Устанавливаем intent, чтобы открыть приложение при нажатии на уведомление
            .setAutoCancel(true) // Уведомление будет автоматически закрыто после нажатия
            .setGroup("flower_helper_group")
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
        notificationManager.notify(-1, summaryNotification)
        notificationManager.notify(notificationId, notification)
        Log.i("Work notif", "All Work")
    }
}