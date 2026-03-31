package com.velsol.feature.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.velsol.feature.messages.generated.resources.Res
import com.velsol.feature.messages.generated.resources.ic_msg_add_circle
import com.velsol.feature.messages.generated.resources.ic_msg_close
import com.velsol.feature.messages.generated.resources.ic_msg_menu
import com.velsol.feature.messages.generated.resources.ic_msg_priority
import com.velsol.feature.messages.generated.resources.ic_msg_search
import com.velsol.feature.messages.generated.resources.messages_cd_add
import com.velsol.feature.messages.generated.resources.messages_cd_clear_search
import com.velsol.feature.messages.generated.resources.messages_cd_menu
import com.velsol.feature.messages.generated.resources.messages_cd_priority
import com.velsol.feature.messages.generated.resources.messages_empty
import com.velsol.feature.messages.generated.resources.messages_search_hint
import com.velsol.feature.messages.generated.resources.messages_tab_incoming
import com.velsol.feature.messages.generated.resources.messages_tab_outgoing
import com.velsol.theme.LocalBrandConfig
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val TabHeight = 48.dp
private val SearchBarHeight = 52.dp
private val FooterHeight = 32.dp
private val TopBarHeight = 56.dp

@Composable
fun MessagesListContent(
    component: MessagesListComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.collectAsState()
    val brandConfig = LocalBrandConfig.current
    val primary = Color(brandConfig.primaryColorArgb)

    val onIntent = remember(component) { { intent: MessagesListIntent -> component.onIntent(intent) } }

    Column(modifier = modifier.fillMaxSize()) {
        MessagesTopBar(
            title = state.headerDate,
            primary = primary,
            onMenuClick = { /* no-op: menu navigation stub */ },
            onAddClick = { onIntent(MessagesListIntent.ClickNewMessage) },
        )

        MessagesTabRow(
            selectedTab = state.selectedTab,
            onSelectTab = { tab -> onIntent(MessagesListIntent.SelectTab(tab)) },
        )

        MessagesSearchBar(
            query = state.searchQuery,
            hint = stringResource(Res.string.messages_search_hint),
            onQueryChange = { q -> onIntent(MessagesListIntent.UpdateSearchQuery(q)) },
            onClear = { onIntent(MessagesListIntent.UpdateSearchQuery("")) },
        )

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = primary,
                    )
                }

                state.messages.isEmpty() -> {
                    Text(
                        text = stringResource(Res.string.messages_empty),
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    )
                }

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.messages, key = { it.id }) { message ->
                            MessageListItem(
                                message = message,
                                primary = primary,
                                onClick = { onIntent(MessagesListIntent.ClickMessage(message.id)) },
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }

        MessagesFooter(
            primary = primary,
            total = state.totalCount,
            unread = state.unreadCount,
        )
    }
}

@Composable
private fun MessagesTopBar(
    title: String,
    primary: Color,
    onMenuClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(TopBarHeight)
            .background(primary),
        contentAlignment = Alignment.Center,
    ) {
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_msg_menu),
                contentDescription = stringResource(Res.string.messages_cd_menu),
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }

        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        IconButton(
            onClick = onAddClick,
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_msg_add_circle),
                contentDescription = stringResource(Res.string.messages_cd_add),
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun MessagesTabRow(
    selectedTab: MessagesTab,
    onSelectTab: (MessagesTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(TabHeight),
    ) {
        for (tab in MessagesTab.entries) {
            val isSelected = selectedTab == tab
            val tabLabel = when (tab) {
                MessagesTab.Incoming -> stringResource(Res.string.messages_tab_incoming)
                MessagesTab.Outgoing -> stringResource(Res.string.messages_tab_outgoing)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (isSelected) Color(0xFF1A1A1A) else Color(0xFFCCCCCC))
                    .clickable(role = Role.Tab) { onSelectTab(tab) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = tabLabel,
                    color = if (isSelected) Color.White else Color(0xFF333333),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun MessagesSearchBar(
    query: String,
    hint: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(SearchBarHeight)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_msg_search),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = hint,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        IconButton(onClick = onClear) {
            Icon(
                painter = painterResource(Res.drawable.ic_msg_close),
                contentDescription = stringResource(Res.string.messages_cd_clear_search),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun MessageListItem(
    message: Message,
    primary: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if (!message.isRead) primary.copy(alpha = 0.05f)
                else MaterialTheme.colorScheme.surface,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (message.isHighPriority) {
            Icon(
                painter = painterResource(Res.drawable.ic_msg_priority),
                contentDescription = stringResource(Res.string.messages_cd_priority),
                tint = primary,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
        } else {
            Spacer(modifier = Modifier.width(26.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = message.subject,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (!message.isRead) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )
            Text(
                text = message.sender.displayName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                maxLines = 1,
            )
            if (message.location.isNotBlank()) {
                Text(
                    text = message.location,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    maxLines = 1,
                )
            }
        }

        if (!message.isRead) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(primary, shape = RoundedCornerShape(50)),
            )
        }
    }
}

@Composable
private fun MessagesFooter(
    primary: Color,
    total: Int,
    unread: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(FooterHeight)
            .background(primary)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Total: $total",
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            text = "Unread: $unread",
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}
