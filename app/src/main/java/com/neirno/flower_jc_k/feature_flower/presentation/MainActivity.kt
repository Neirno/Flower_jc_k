package com.neirno.flower_jc_k.feature_flower.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.neirno.flower_jc_k.feature_flower.domain.util.FlowerOrder
import com.neirno.flower_jc_k.feature_flower.domain.util.OrderType
//import com.neirno.flower_jc_k.AddFlowerPage
//import com.neirno.flower_jc_k.MainPage
import com.neirno.flower_jc_k.feature_flower.presentation.flowers.FlowersScreen
import com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.AddEditFlowerScreen
import com.neirno.flower_jc_k.feature_flower.presentation.add_edit_flower.AddEditFlowerViewModel
import com.neirno.flower_jc_k.feature_flower.presentation.camera.CameraScreen
import com.neirno.flower_jc_k.feature_flower.presentation.camera.CameraViewModel
import com.neirno.flower_jc_k.feature_flower.presentation.flowers.FlowerViewModel
import com.neirno.flower_jc_k.feature_flower.presentation.util.Screen
import com.neirno.flower_jc_k.ui.theme.CustomBrown
import com.neirno.flower_jc_k.ui.theme.CustomWhite
import com.neirno.flower_jc_k.ui.theme.Flower_jc_kTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.CAMERA,
        )
    } else {
        arrayOf(
            Manifest.permission.CAMERA
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            Flower_jc_kTheme {
                AppContent()
            }
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun AppContent() {
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
        HandlePermissions(dialogQueue, viewModel, multiplePermissionResultLauncher)

        LaunchedEffect(Unit) {
            multiplePermissionResultLauncher.launch(permissionsToRequest)
        }

        Surface(color = CustomWhite) {
            Navigation(navController = rememberNavController())
        }
    }

    @Composable
    fun Navigation(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = Screen.FlowersScreen.route
        ) {
            composable(route = Screen.CameraScreen.route) {
                val cameraViewModel: CameraViewModel = hiltViewModel()
                CameraScreen(
                    navController = navController,
                    viewState = cameraViewModel.viewState.value,
                    onEvent = cameraViewModel::onEvent
                )
            }
            composable(route = Screen.FlowersScreen.route) {
                val flowerViewModel: FlowerViewModel = hiltViewModel()
                FlowersScreen(
                    navController = navController,
                    viewState = flowerViewModel.viewState.value,
                    onEvent = flowerViewModel::onEvent
                )
            }
            composable(
                route = Screen.AddEditFlowerScreen.route + "?flowerId={flowerId}",
                arguments = listOf(
                    navArgument(name = "flowerId") {
                        type = NavType.IntType
                        defaultValue = -1L
                    },
                )
            ) {
                val addEditFlowerViewModel: AddEditFlowerViewModel = hiltViewModel()
                AddEditFlowerScreen(
                    navController = navController,
                    viewState = addEditFlowerViewModel.viewState.value,
                    onEvent = addEditFlowerViewModel::onEvent,
                    eventsFlow = addEditFlowerViewModel.eventFlow
                )
            }
        }

    }

    @Composable
    fun HandlePermissions(
        dialogQueue: List<String>,
        viewModel: MainViewModel,
        multiplePermissionResultLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>
    ) {
        dialogQueue
            .reversed()
            .forEach { permission ->
                PermissionDialog(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = CustomBrown)
                        .padding(16.dp),
                    permissionTextProvider = when (permission) {
                        Manifest.permission.POST_NOTIFICATIONS -> {
                            NotificationPermissionTextProvider()
                        }
                        Manifest.permission.CAMERA -> {
                            CameraPermissionTextProvider()
                        }
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

    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}
