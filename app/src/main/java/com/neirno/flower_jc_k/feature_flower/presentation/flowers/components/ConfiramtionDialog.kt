package com.neirno.flower_jc_k.feature_flower.presentation.flowers.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.neirno.flower_jc_k.R

@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    title: String,
    description: String,
    onHide: () -> Unit,
    onConfirm: () -> Unit)
{
    if (showDialog) {
        Dialog(
            onDismissRequest = onHide
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = title, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = description, style = MaterialTheme.typography.bodyMedium)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onHide
                        ) {
                            Text(text = stringResource(id = R.string.cancel), style = MaterialTheme.typography.labelSmall)
                        }
                        TextButton(
                            onClick = {
                                onConfirm()
                                onHide()
                            }
                        ) {
                            Text(text = stringResource(id = R.string.confirm), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}