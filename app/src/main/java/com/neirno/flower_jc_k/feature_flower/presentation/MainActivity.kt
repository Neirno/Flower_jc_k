package com.neirno.flower_jc_k.feature_flower.presentation

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.neirno.flower_jc_k.R
//import com.neirno.flower_jc_k.AddFlowerPage
//import com.neirno.flower_jc_k.MainPage
import com.neirno.flower_jc_k.feature_flower.presentation.flowers.FlowersScreen
import com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.AddEditFlowerScreen
import com.neirno.flower_jc_k.feature_flower.presentation.camera.CameraScreen
import com.neirno.flower_jc_k.feature_flower.presentation.util.Screen
import com.neirno.flower_jc_k.ui.theme.Flower_jc_kTheme
import dagger.hilt.android.AndroidEntryPoint


fun createNotification(context: Context) {
    val channelId = "simple_notification_channel"
    val channelName = "Simple Notification"
    val notificationId = 1

    // Создание канала уведомлений для Android Oreo и выше
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(channelId, channelName, importance)
    val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)

    // Создание уведомления
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_flower_list) // Замените на свою иконку
        .setContentTitle("My notification")
        .setContentText("Hello World!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    // Отображение уведомления
    with(NotificationManagerCompat.from(context)) {
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
            Log.i("1","ГОООООООООООООООЙДАААААААА")
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notify(notificationId, builder.build())
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.CAMERA,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    } else {
        TODO("VERSION.SDK_INT < TIRAMISU")
            //Log.i("ггГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГГ","")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // тут запрос на все уведомления

            createNotification(application.applicationContext)
            Flower_jc_kTheme {
                val context = LocalContext.current

                val viewModel = viewModel<MainViewModel>()
                val dialogQueue = viewModel.visiblePermissionDialogQueue

                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { perms ->
                        permissionsToRequest.forEach { permission ->
                            viewModel.onPermissionResult(
                                permission = permission,
                                isGranted = perms[permission] == true
                            )
                        }
                    }
                )

                dialogQueue
                    .reversed()
                    .forEach { permission ->
                        PermissionDialog(
                            modifier = Modifier.background(color = Color.White),
                            permissionTextProvider = when (permission) {
                                Manifest.permission.POST_NOTIFICATIONS -> {
                                    NotificationPermissionTextProvider()
                                }
                                Manifest.permission.CAMERA -> {
                                    CameraPermissionTextProvider()
                                }
                                Manifest.permission.USE_EXACT_ALARM -> {
                                    ExactAlarmPermissionTextProvider()
                                }
                                /*Manifest.permission.MANAGE_EXTERNAL_STORAGE -> {
                                    StoragePermissionTextProvider()
                                }*/
                                else -> return@forEach
                            },
                            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                permission
                            ),
                            onDismiss = viewModel::dismissDialog,
                            onOkClick = {
                                viewModel.dismissDialog()
                                multiplePermissionResultLauncher.launch(
                                    arrayOf(permission)
                                )
                            },
                            onGoToAppSettingsClick = ::openAppSettings
                        )
                    }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
                    if (alarmManager?.canScheduleExactAlarms() == false) {
                        Intent().also { intent ->
                            intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                            context.startActivity(intent)
                        }
                    }
                }

                Surface(
                    color = Color(0xFFDBD2C0)/*MaterialTheme.colorScheme.background*/,
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.FlowersScreen.route
                    ) {
                        composable(route = Screen.CameraScreen.route) {
                            CameraScreen(navController = navController)
                        }
                        composable(route = Screen.FlowersScreen.route) {

                            multiplePermissionResultLauncher.launch(permissionsToRequest)
                            FlowersScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditFlowerScreen.route +
                                    "?flowerId={flowerId}",
                            arguments = listOf(
                                navArgument(
                                    name = "flowerId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                            )
                        ) {
                            AddEditFlowerScreen(
                                navController = navController,
                            )
                        }
                    }
                }
            }
        }
    }
}
fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}
