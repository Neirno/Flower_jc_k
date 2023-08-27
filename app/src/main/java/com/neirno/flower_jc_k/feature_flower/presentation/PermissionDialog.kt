package com.neirno.flower_jc_k.feature_flower.presentation


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Permission required", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = permissionTextProvider.getDescription(
                        isPermanentlyDeclined = isPermanentlyDeclined
                    ),
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Text(
                    text = if(isPermanentlyDeclined) {
                        "Grant permission"
                    } else {
                        "OK"
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermanentlyDeclined) {
                                onGoToAppSettingsClick()
                            } else {
                                onOkClick()
                            }
                        }
                        .padding(16.dp)
                )
            }
        },
        modifier = modifier
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class CameraPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "Похоже, вы навсегда отказались от разрешения на камеру. " +
                    "Вы можете перейти в настройки приложения, чтобы предоставить его."
        } else {
            "Этому приложению нужен доступ к вашей камере, чтобы ваши друзья " +
                    "могли видеть вас в звонке."
        }
    }
}

class NotificationPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "Похоже, вы навсегда отказались от разрешения на уведомления. " +
                    "Вы можете перейти в настройки приложения, чтобы предоставить его."
        } else {
            "Этому приложению нужен доступ к уведомлениям, чтобы вы могли получать " +
                    "уведомления от нас."
        }
    }
}

class ExactAlarmPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "Похоже, вы навсегда отказались от разрешения на точные будильники. " +
                    "Вы можете перейти в настройки приложения, чтобы предоставить его."
        } else {
            "Этому приложению нужен доступ к точным будильникам, чтобы вы могли " +
                    "получать уведомления в точное время."
        }
    }
}

class StoragePermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "Похоже, вы навсегда отказались от разрешения на доступ к хранилищу. " +
                    "Вы можете перейти в настройки приложения, чтобы предоставить его."
        } else {
            "Этому приложению нужен доступ к вашему хранилищу, чтобы сохранять и загружать " +
                    "файлы, такие как изображения."
        }
    }
}
