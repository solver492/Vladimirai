package com.ai.venice.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.venice.model.ChatMessage
import com.ai.venice.model.Mind
import com.ai.venice.model.VeniceModel
import com.ai.venice.ui.theme.TextMuted
import com.ai.venice.ui.theme.TextPrimary
import com.ai.venice.ui.theme.TextSecondary
import com.ai.venice.ui.theme.VeniceBlue
import com.ai.venice.ui.theme.VeniceBorder
import com.ai.venice.ui.theme.VeniceBorderLight
import com.ai.venice.ui.theme.VeniceCoral
import com.ai.venice.ui.theme.VeniceCyan
import com.ai.venice.ui.theme.VeniceGreen
import com.ai.venice.ui.theme.VeniceSurface
import com.ai.venice.ui.theme.VeniceSurfaceContainer
import com.ai.venice.ui.theme.VeniceSurfaceVariant
import com.ai.venice.ui.theme.VeniceVoid
import com.ai.venice.util.ResourceUtils
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    selectedModel: VeniceModel,
    selectedMind: Mind,
    allModels: List<VeniceModel>,
    allMinds: List<Mind>,
    isGenerating: Boolean,
    onModelSelected: (VeniceModel) -> Unit,
    onMindSelected: (Mind) -> Unit,
    onSendMessage: (String) -> Unit,
    onNavigateToMinds: () -> Unit
) {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    var showModelSheet by remember { mutableStateOf(false) }
    var showMindSheet by remember { mutableStateOf(false) }
    var webSearchEnabled by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size, isGenerating) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VeniceVoid)
    ) {
        // Model and Mind Control Strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(VeniceSurface)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Model Selector Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(VeniceSurfaceVariant)
                    .border(1.dp, VeniceBorder, RoundedCornerShape(20.dp))
                    .clickable { showModelSheet = true }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (selectedModel.isUncensored) VeniceCoral else VeniceCyan)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = selectedModel.name,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Model",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Mind Selector Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(VeniceSurfaceVariant)
                    .border(1.dp, VeniceBorder, RoundedCornerShape(20.dp))
                    .clickable { showMindSheet = true }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "Current Mind",
                    tint = VeniceCyan,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = selectedMind.name,
                    color = VeniceCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Mind",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (messages.isEmpty()) {
                // Empty Chat Hero
                ChatEmptyState(
                    mind = selectedMind,
                    model = selectedModel,
                    onPromptSuggestionClick = { prompt ->
                        onSendMessage(prompt)
                    }
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(messages, key = { it.id }) { message ->
                        ChatMessageItem(
                            message = message,
                            onCopyText = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Vlad AI", message.content)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    if (isGenerating) {
                        item(key = "generating_indicator") {
                            GeneratingIndicator(modelName = selectedModel.name)
                        }
                    }
                }
            }
        }

        // Input Area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(VeniceSurface)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Optional Web Search & Privacy Chip row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (webSearchEnabled) VeniceSurfaceContainer else VeniceVoid)
                        .border(1.dp, if (webSearchEnabled) VeniceCyan else VeniceBorder, RoundedCornerShape(12.dp))
                        .clickable { webSearchEnabled = !webSearchEnabled }
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Web Search",
                        tint = if (webSearchEnabled) VeniceCyan else TextMuted,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (webSearchEnabled) "Web Search ON" else "Web Search OFF",
                        color = if (webSearchEnabled) VeniceCyan else TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Encrypted",
                        tint = TextMuted,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Ephemeral Memory",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            // Input Box + Send Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = "Ask ${selectedMind.name} anything privately...",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = VeniceVoid,
                        unfocusedContainerColor = VeniceVoid,
                        disabledContainerColor = VeniceVoid,
                        focusedBorderColor = VeniceCyan,
                        unfocusedBorderColor = VeniceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.width(8.dp))

                val canSend = inputText.isNotBlank() && !isGenerating
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (canSend) {
                                Brush.linearGradient(listOf(VeniceCyan, VeniceBlue))
                            } else {
                                Brush.linearGradient(listOf(VeniceSurfaceVariant, VeniceSurfaceVariant))
                            }
                        )
                        .clickable(enabled = canSend) {
                            val text = inputText
                            inputText = ""
                            onSendMessage(text)
                        }
                        .testTag("chat_send_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Message",
                        tint = if (canSend) VeniceVoid else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    // Model Selector Bottom Sheet
    if (showModelSheet) {
        ModalBottomSheet(
            onDismissRequest = { showModelSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = VeniceSurface,
            scrimColor = Color.Black.copy(alpha = 0.7f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "SELECT VENICE MODEL",
                    color = VeniceCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Open, uncensored weights hosted on sovereign infrastructure",
                    color = TextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                allModels.forEach { model ->
                    val isSelected = model.id == selectedModel.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) VeniceSurfaceVariant else Color.Transparent)
                            .border(1.dp, if (isSelected) VeniceCyan else VeniceBorder, RoundedCornerShape(12.dp))
                            .clickable {
                                onModelSelected(model)
                                showModelSheet = false
                            }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = model.name,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (model.isUncensored) VeniceCoral.copy(alpha = 0.2f) else VeniceCyan.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = model.badge,
                                        color = if (model.isUncensored) VeniceCoral else VeniceCyan,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = model.description,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = VeniceCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }

    // Mind Selector Bottom Sheet
    if (showMindSheet) {
        ModalBottomSheet(
            onDismissRequest = { showMindSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = VeniceSurface,
            scrimColor = Color.Black.copy(alpha = 0.7f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "VENICE MINDS",
                            color = VeniceCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Specialized AI personas with unique directives",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        text = "View All →",
                        color = VeniceCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            showMindSheet = false
                            onNavigateToMinds()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                allMinds.take(5).forEach { mind ->
                    val isSelected = mind.id == selectedMind.id
                    val drawableId = ResourceUtils.getDrawableIdByName(context, mind.graphicDrawableName)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) VeniceSurfaceVariant else Color.Transparent)
                            .border(1.dp, if (isSelected) VeniceCyan else VeniceBorder, RoundedCornerShape(12.dp))
                            .clickable {
                                onMindSelected(mind)
                                showMindSheet = false
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SafeChatImage(
                            drawableId = drawableId,
                            contentDescription = mind.name,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = mind.name,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = mind.tagLine,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = VeniceCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

@Composable
fun ChatEmptyState(
    mind: Mind,
    model: VeniceModel,
    onPromptSuggestionClick: (String) -> Unit
) {
    val context = LocalContext.current
    val graphicId = ResourceUtils.getDrawableIdByName(context, mind.graphicDrawableName)

    val starterPrompts = when (mind.id) {
        "mind_uncensored" -> listOf(
            "Give me an objective, uncensored analysis of open-source AI vs centralized labs",
            "What are the most overlooked privacy risks in modern smartphones?",
            "Explain the concept of sovereign individual technology"
        )
        "mind_coder" -> listOf(
            "Write a robust Kotlin StateFlow and Coroutine worker architecture",
            "Compare memory safety between Rust and modern modern languages",
            "How do I optimize Jetpack Compose recompositions in large lists?"
        )
        "mind_crypto" -> listOf(
            "Explain how Zero-Knowledge Succinct Non-Interactive Arguments of Knowledge work",
            "Compare account abstraction in Ethereum vs Solana state compression",
            "What is private peer-to-peer digital cash in 2026?"
        )
        "mind_philosopher" -> listOf(
            "Does human consciousness require biological substrate?",
            "Examine free will through the lens of determinism and quantum indeterminacy",
            "What is truth in an era of synthetic media?"
        )
        else -> listOf(
            "Write an atmospheric cyberpunk story set in Neo-Vlad",
            "What makes Vlad AI's zero-logging architecture unique?",
            "Draft a comprehensive research breakdown on decentralized compute"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SafeChatImage(
            drawableId = graphicId,
            contentDescription = mind.name,
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, VeniceBorderLight, RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = mind.name,
            color = TextPrimary,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )

        Text(
            text = mind.tagLine,
            color = TextSecondary,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(VeniceSurfaceVariant)
                .border(1.dp, VeniceBorder, RoundedCornerShape(14.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = VeniceGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Active Model: ${model.name} (${model.badge})",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "PROMPT STARTERS",
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        starterPrompts.forEach { prompt ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(VeniceSurface)
                    .border(1.dp, VeniceBorder, RoundedCornerShape(12.dp))
                    .clickable { onPromptSuggestionClick(prompt) }
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = VeniceCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = prompt,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    onCopyText: () -> Unit
) {
    val isUser = message.role == "user"
    val timeStr = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(message.timestamp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(VeniceSurfaceContainer)
                    .border(1.dp, VeniceCyan.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "V",
                    color = VeniceCyan,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.fillMaxWidth(if (isUser) 0.85f else 0.92f),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            if (!isUser && message.modelName.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = message.mindName.ifEmpty { "Vlad AI" },
                        color = VeniceCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${message.modelName}",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        )
                    )
                    .background(
                        if (isUser) VeniceSurfaceContainer
                        else VeniceSurfaceVariant
                    )
                    .border(
                        1.dp,
                        if (isUser) VeniceBorderLight else VeniceBorder,
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        )
                    )
                    .padding(14.dp)
            ) {
                SelectionContainer {
                    Text(
                        text = message.content,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Row(
                modifier = Modifier.padding(top = 4.dp, start = 2.dp, end = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = timeStr,
                    color = TextMuted,
                    fontSize = 10.sp
                )

                if (!isUser) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy message",
                        tint = TextMuted,
                        modifier = Modifier
                            .size(12.dp)
                            .clickable { onCopyText() }
                    )
                }
            }
        }
    }
}

@Composable
fun GeneratingIndicator(modelName: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(VeniceSurfaceContainer)
                .alpha(alpha),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "V",
                color = VeniceCyan,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$modelName is reasoning...",
            color = VeniceCyan,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.alpha(alpha)
        )
    }
}

@Composable
fun SafeChatImage(
    drawableId: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    if (drawableId != 0) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        Box(
            modifier = modifier.background(VeniceSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = VeniceCyan,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
