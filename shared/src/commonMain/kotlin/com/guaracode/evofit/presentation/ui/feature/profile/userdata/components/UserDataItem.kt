package com.guaracode.evofit.presentation.ui.feature.profile.userdata.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import com.guaracode.evofit.presentation.ui.theme.evoColors
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.cancel
import evofit.shared.generated.resources.confirm
import evofit.shared.generated.resources.edit
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UserDataItem(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChangeConfirmed: (String) -> Unit = {},
    customEditAction: (() -> Unit)? = null
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedValue by remember { mutableStateOf(value) }

    val isInputValid = editedValue.isNotBlank()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.evoColors.green,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (isEditing && customEditAction == null) {
                OutlinedTextField(
                    value = editedValue,
                    onValueChange = { editedValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = !isInputValid && editedValue.isNotEmpty(),
                    keyboardOptions = keyboardOptions,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.evoColors.green,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        cursorColor = MaterialTheme.evoColors.green,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            } else {
                Text(
                    text = value.ifBlank { "---" },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        AnimatedVisibility(
            visible = isEditing && customEditAction == null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row {
                IconButton(
                    enabled = isInputValid,
                    onClick = {
                        isEditing = false
                        onValueChangeConfirmed(editedValue)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(Res.string.confirm),
                        tint = if (isInputValid) MaterialTheme.evoColors.green else Color.Gray
                    )
                }

                IconButton(
                    onClick = {
                        isEditing = false
                        editedValue = value
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(Res.string.cancel),
                        tint = Color.Red
                    )
                }
            }
        }

        if (!isEditing || customEditAction != null) {
            IconButton(
                onClick = {
                    if (customEditAction != null) {
                        customEditAction()
                    } else {
                        isEditing = true
                        editedValue = value
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(Res.string.edit),
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun UserDataItemPreview() {
    EvoFitTheme {
        UserDataItem(
            icon = Icons.Default.Person,
            title = "Nome",
            value = "Wesley Lopes"
        )
    }
}
