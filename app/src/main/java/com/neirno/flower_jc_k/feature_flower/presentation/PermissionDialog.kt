package com.neirno.flower_jc_k.feature_flower.presentation


import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neirno.flower_jc_k.R
import com.neirno.flower_jc_k.feature_flower.presentation.components.ButtonType
import com.neirno.flower_jc_k.ui.theme.CustomDark

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
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        content = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row (modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = stringResource(R.string.permission_requied), style = MaterialTheme.typography.bodyMedium, color = CustomDark,)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(painter = ButtonType.CLOSE.imageProvider(), contentDescription = ButtonType.CLOSE.description)
                    }                    
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = permissionTextProvider.getDescription(
                        isPermanentlyDeclined = isPermanentlyDeclined,
                        context = context
                    ),
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Text(
                    color = CustomDark,
                    text = if(isPermanentlyDeclined) {
                        stringResource(R.string.grant_permisson)
                    } else {
                        stringResource(R.string.ok)
                    },
                    style = MaterialTheme.typography.bodySmall,
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
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean, context: Context): String
}

class CameraPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean, context: Context): String {
        return if(isPermanentlyDeclined) {
            context.getString(R.string.camera_permission_error)
        } else {
            context.getString(R.string.camera_permission)
        }
    }
}

class NotificationPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean, context: Context): String {
        return if(isPermanentlyDeclined) {
            context.getString(R.string.notification_permission_error)
        } else {
            context.getString(R.string.notification_permission)
        }
    }
}
/*

class ExactAlarmPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean, context: Context): String {
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
    override fun getDescription(isPermanentlyDeclined: Boolean, context: Context): String {
        return if(isPermanentlyDeclined) {
            "Похоже, вы навсегда отказались от разрешения на доступ к хранилищу. " +
                    "Вы можете перейти в настройки приложения, чтобы предоставить его."
        } else {
            "Этому приложению нужен доступ к вашему хранилищу, чтобы сохранять и загружать " +
                    "файлы, такие как изображения."
        }
    }
}
*/
