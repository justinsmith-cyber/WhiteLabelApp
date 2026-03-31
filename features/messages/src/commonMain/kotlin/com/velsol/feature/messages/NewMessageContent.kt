package com.velsol.feature.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.velsol.feature.messages.generated.resources.Res
import com.velsol.feature.messages.generated.resources.ic_msg_arrow_back
import com.velsol.feature.messages.generated.resources.messages_cd_back
import com.velsol.feature.messages.generated.resources.messages_field_body_hint
import com.velsol.feature.messages.generated.resources.messages_field_from
import com.velsol.feature.messages.generated.resources.messages_field_high_priority
import com.velsol.feature.messages.generated.resources.messages_field_location
import com.velsol.feature.messages.generated.resources.messages_field_subject
import com.velsol.feature.messages.generated.resources.messages_field_to
import com.velsol.feature.messages.generated.resources.messages_new_message_title
import com.velsol.feature.messages.generated.resources.messages_send_action
import com.velsol.feature.messages.generated.resources.messages_send_error
import com.velsol.feature.messages.generated.resources.messages_subject_required_error
import com.velsol.theme.LocalBrandConfig
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val FormFieldHeight = 52.dp
private val LabelWidth = 100.dp

@Composable
fun NewMessageContent(
    component: NewMessageComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.collectAsState()
    val brandConfig = LocalBrandConfig.current
    val primary = Color(brandConfig.primaryColorArgb)

    val snackbarHostState = remember { SnackbarHostState() }
    val sendErrorMessage = stringResource(Res.string.messages_send_error)

    LaunchedEffect(state.sendError) {
        if (state.sendError) {
            snackbarHostState.showSnackbar(sendErrorMessage)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            NewMessageTopBar(
                primary = primary,
                isSending = state.isSending,
                onBackClick = { component.onIntent(NewMessageIntent.ClickBack) },
                onSendClick = { component.onIntent(NewMessageIntent.ClickSend) },
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                FormSection {
                    ReadOnlyFormRow(
                        label = stringResource(Res.string.messages_field_to),
                        value = state.to.name,
                    )
                    HorizontalDivider()
                    ReadOnlyFormRow(
                        label = stringResource(Res.string.messages_field_from),
                        value = state.from.displayName,
                    )
                    HorizontalDivider()
                    EditableFormRow(
                        label = stringResource(Res.string.messages_field_subject),
                        value = state.subject,
                        isError = state.sendError && state.subject.isBlank(),
                        errorMessage = stringResource(Res.string.messages_subject_required_error),
                        onValueChange = { component.onIntent(NewMessageIntent.UpdateSubject(it)) },
                    )
                    HorizontalDivider()
                    EditableFormRow(
                        label = stringResource(Res.string.messages_field_location),
                        value = state.location,
                        isError = false,
                        errorMessage = null,
                        onValueChange = { component.onIntent(NewMessageIntent.UpdateLocation(it)) },
                    )
                    HorizontalDivider()
                    HighPriorityRow(
                        label = stringResource(Res.string.messages_field_high_priority),
                        checked = state.isHighPriority,
                        primary = primary,
                        onCheckedChange = { component.onIntent(NewMessageIntent.ToggleHighPriority(it)) },
                    )
                }

                HorizontalDivider()

                MessageBodyField(
                    value = state.body,
                    hint = stringResource(Res.string.messages_field_body_hint),
                    onValueChange = { component.onIntent(NewMessageIntent.UpdateBody(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .height(240.dp),
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun NewMessageTopBar(
    primary: Color,
    isSending: Boolean,
    onBackClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(primary),
        contentAlignment = Alignment.Center,
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_msg_arrow_back),
                contentDescription = stringResource(Res.string.messages_cd_back),
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }

        Text(
            text = stringResource(Res.string.messages_new_message_title),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        if (isSending) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .size(20.dp),
            )
        } else {
            TextButton(
                onClick = onSendClick,
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                Text(
                    text = stringResource(Res.string.messages_send_action),
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun FormSection(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        content()
    }
}

@Composable
private fun ReadOnlyFormRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(FormFieldHeight)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(LabelWidth, FormFieldHeight).padding(top = 16.dp),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun EditableFormRow(
    label: String,
    value: String,
    isError: Boolean,
    errorMessage: String?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(FormFieldHeight)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (isError) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(LabelWidth, FormFieldHeight).padding(top = 16.dp),
            )
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier.weight(1f),
            )
        }
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp + LabelWidth, bottom = 4.dp),
            )
        }
    }
}

@Composable
private fun HighPriorityRow(
    label: String,
    checked: Boolean,
    primary: Color,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(FormFieldHeight)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = primary,
            ),
        )
    }
}

@Composable
private fun MessageBodyField(
    value: String,
    hint: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
    ) {
        if (value.isEmpty()) {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxSize(),
        )
    }
}
