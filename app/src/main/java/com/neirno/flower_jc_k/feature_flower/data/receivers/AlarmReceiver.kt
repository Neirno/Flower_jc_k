package com.neirno.flower_jc_k.feature_flower.data.receivers

import android.Manifest
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

        val notification = NotificationCompat.Builder(context, "flower_helper")
            .setSmallIcon(R.drawable.ic_notification) // Replace with your own icon
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("$actionName $flowerName!")
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
}