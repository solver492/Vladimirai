package com.ai.venice.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.venice.R
import com.ai.venice.ui.theme.TextMuted
import com.ai.venice.ui.theme.TextPrimary
import com.ai.venice.ui.theme.TextSecondary
import com.ai.venice.ui.theme.VeniceBorder
import com.ai.venice.ui.theme.VeniceCyan
import com.ai.venice.ui.theme.VeniceGreen
import com.ai.venice.ui.theme.VeniceSurface
import com.ai.venice.ui.theme.VeniceSurfaceVariant
import com.ai.venice.ui.theme.VeniceVoid

@Composable
fun VeniceTopBar(
    currentTab: String,
    onNewChatClick: () -> Unit
) {
    Surface(
        color = VeniceVoid,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Vlad AI Brand Logo + Name
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.vlad_ai_logo),
                        contentDescription = "Vlad AI Logo",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, VeniceCyan, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "VLAD",
                                color = TextPrimary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 17.sp,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "AI",
                                color = VeniceCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        Text(
                            text = "PRIVATE & UNFILTERED",
                            color = TextMuted,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Privacy Indicator Pill & Action
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(VeniceSurfaceVariant)
                            .border(1.dp, VeniceBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(VeniceGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ZERO LOGS",
                                color = VeniceGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    if (currentTab == "chat") {
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = onNewChatClick,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(VeniceSurfaceVariant)
                                .border(1.dp, VeniceBorder, RoundedCornerShape(8.dp))
                                .testTag("new_chat_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "New Conversation",
                                tint = VeniceCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(VeniceBorder)
            )
        }
    }
}

data class NavigationTabItem(
    val key: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun VeniceBottomBar(
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf(
        NavigationTabItem("chat", "Chat", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline),
        NavigationTabItem("images", "Studio", Icons.Filled.Image, Icons.Outlined.Image),
        NavigationTabItem("minds", "Minds", Icons.Filled.Psychology, Icons.Outlined.Psychology),
        NavigationTabItem("history", "History", Icons.Filled.History, Icons.Outlined.History),
        NavigationTabItem("settings", "Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
    )

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(VeniceBorder)
        )
        NavigationBar(
            containerColor = VeniceVoid,
            tonalElevation = 0.dp,
            modifier = Modifier.height(64.dp)
        ) {
            tabs.forEach { tab ->
                val selected = currentTab == tab.key
                NavigationBarItem(
                    selected = selected,
                    onClick = { onTabSelected(tab.key) },
                    icon = {
                        Icon(
                            imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = tab.title,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = tab.title,
                            fontSize = 11.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VeniceCyan,
                        selectedTextColor = VeniceCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted,
                        indicatorColor = VeniceSurfaceVariant
                    ),
                    modifier = Modifier.testTag("nav_tab_${tab.key}")
                )
            }
        }
    }
}
