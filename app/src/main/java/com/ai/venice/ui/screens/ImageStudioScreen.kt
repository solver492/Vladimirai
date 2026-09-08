package com.ai.venice.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ai.venice.model.GeneratedArt
import com.ai.venice.model.ImageStylePreset
import com.ai.venice.ui.theme.TextMuted
import com.ai.venice.ui.theme.TextPrimary
import com.ai.venice.ui.theme.TextSecondary
import com.ai.venice.ui.theme.VeniceBlue
import com.ai.venice.ui.theme.VeniceBorder
import com.ai.venice.ui.theme.VeniceBorderLight
import com.ai.venice.ui.theme.VeniceCyan
import com.ai.venice.ui.theme.VeniceSurface
import com.ai.venice.ui.theme.VeniceSurfaceContainer
import com.ai.venice.ui.theme.VeniceSurfaceVariant
import com.ai.venice.ui.theme.VeniceVoid
import com.ai.venice.util.ResourceUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageStudioScreen(
    presets: List<ImageStylePreset>,
    selectedPreset: ImageStylePreset,
    generatedArts: List<GeneratedArt>,
    isGenerating: Boolean,
    onPresetSelected: (ImageStylePreset) -> Unit,
    onGenerate: (prompt: String, preset: ImageStylePreset, aspectRatio: String) -> Unit,
    onDeleteArt: (String) -> Unit
) {
    val context = LocalContext.current
    var promptText by remember { mutableStateOf("") }
    var negativePrompt by remember { mutableStateOf("") }
    var showNegativePrompt by remember { mutableStateOf(false) }
    var selectedAspectRatio by remember { mutableStateOf("1:1") }
    var previewArt by remember { mutableStateOf<GeneratedArt?>(null) }

    val aspectRatios = listOf("1:1", "9:16", "16:9", "4:3")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(VeniceVoid),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Studio Header
        item {
            Column {
                Text(
                    text = "VLAD ART STUDIO",
                    color = VeniceCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Uncensored Generative Media",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Create vivid imagery without algorithmic censorship or content filters.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // Prompt Input Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VeniceSurface)
                    .border(1.dp, VeniceBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = "PROMPT",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = promptText,
                    onValueChange = { promptText = it },
                    placeholder = {
                        Text(
                            text = "Describe what you want to create... (e.g., A cybernetic samurai in rain-slicked Tokyo)",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("image_prompt_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = VeniceVoid,
                        unfocusedContainerColor = VeniceVoid,
                        focusedBorderColor = VeniceCyan,
                        unfocusedBorderColor = VeniceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Negative Prompt Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showNegativePrompt = !showNegativePrompt }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (showNegativePrompt) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Negative Prompt (What to avoid)",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                AnimatedVisibility(visible = showNegativePrompt) {
                    OutlinedTextField(
                        value = negativePrompt,
                        onValueChange = { negativePrompt = it },
                        placeholder = {
                            Text(
                                text = "blurry, low quality, distorted hands, extra limbs",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VeniceVoid,
                            unfocusedContainerColor = VeniceVoid,
                            focusedBorderColor = VeniceCyan,
                            unfocusedBorderColor = VeniceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )
                }
            }
        }

        // Style Presets Carousel
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STYLE PRESET",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = selectedPreset.name,
                        color = VeniceCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(presets, key = { it.id }) { preset ->
                        val isSelected = preset.id == selectedPreset.id
                        val drawableId = ResourceUtils.getDrawableIdByName(context, preset.drawableName)

                        Column(
                            modifier = Modifier
                                .width(90.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(VeniceSurface)
                                .border(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) VeniceCyan else VeniceBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onPresetSelected(preset) }
                                .padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SafeStudioImage(
                                drawableId = drawableId,
                                contentDescription = preset.name,
                                modifier = Modifier
                                    .size(78.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = preset.name,
                                color = if (isSelected) VeniceCyan else TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // Aspect Ratio Selection
        item {
            Column {
                Text(
                    text = "ASPECT RATIO",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    aspectRatios.forEach { ratio ->
                        val isSelected = ratio == selectedAspectRatio
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) VeniceSurfaceVariant else VeniceSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) VeniceCyan else VeniceBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedAspectRatio = ratio }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ratio,
                                color = if (isSelected) VeniceCyan else TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // Generate Action Button
        item {
            val canGenerate = promptText.isNotBlank() && !isGenerating
            Button(
                onClick = {
                    onGenerate(promptText, selectedPreset, selectedAspectRatio)
                },
                enabled = canGenerate,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .testTag("generate_image_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VeniceCyan,
                    disabledContainerColor = VeniceSurfaceVariant
                )
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = VeniceVoid,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Synthesizing with Vlad Engine...",
                        color = VeniceVoid,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = if (canGenerate) VeniceVoid else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Generate Image",
                        color = if (canGenerate) VeniceVoid else TextMuted,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }

        // Recent Creations Gallery
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GALLERY (${generatedArts.size})",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Tap to view / reuse",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }

        // Gallery Items (Display 2 per row)
        val chunked = generatedArts.chunked(2)
        items(chunked) { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                pair.forEach { art ->
                    val drawableId = ResourceUtils.getDrawableIdByName(context, art.drawableName)

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(VeniceSurface)
                            .border(1.dp, VeniceBorder, RoundedCornerShape(14.dp))
                            .clickable { previewArt = art }
                            .padding(8.dp)
                    ) {
                        SafeStudioImage(
                            drawableId = drawableId,
                            contentDescription = art.prompt,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = art.styleName,
                            color = VeniceCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = art.prompt,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    // Image Preview Dialog
    previewArt?.let { art ->
        val drawableId = ResourceUtils.getDrawableIdByName(context, art.drawableName)

        Dialog(onDismissRequest = { previewArt = null }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp)),
                color = VeniceSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, VeniceBorderLight)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = art.styleName,
                                color = VeniceCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(VeniceSurfaceVariant)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = art.aspectRatio,
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = { previewArt = null },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    SafeStudioImage(
                        drawableId = drawableId,
                        contentDescription = art.prompt,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "PROMPT",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = art.prompt,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                promptText = art.prompt
                                previewArt = null
                                Toast.makeText(context, "Prompt copied to editor", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VeniceSurfaceVariant),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = null,
                                tint = VeniceCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Reuse Prompt", color = VeniceCyan, fontSize = 12.sp)
                        }

                        IconButton(
                            onClick = {
                                onDeleteArt(art.id)
                                previewArt = null
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SafeStudioImage(
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
                imageVector = Icons.Default.Image,
                contentDescription = null,
                tint = VeniceCyan,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
