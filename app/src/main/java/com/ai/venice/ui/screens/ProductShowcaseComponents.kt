package com.ai.venice.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.venice.model.ChatMessage
import com.ai.venice.model.ProductSample
import com.ai.venice.ui.theme.TextMuted
import com.ai.venice.ui.theme.TextPrimary
import com.ai.venice.ui.theme.TextSecondary
import com.ai.venice.ui.theme.VeniceBorder
import com.ai.venice.ui.theme.VeniceBorderLight
import com.ai.venice.ui.theme.VeniceCoral
import com.ai.venice.ui.theme.VeniceCyan
import com.ai.venice.ui.theme.VeniceGreen
import com.ai.venice.ui.theme.VeniceSurface
import com.ai.venice.ui.theme.VeniceSurfaceContainer
import com.ai.venice.ui.theme.VeniceSurfaceVariant
import com.ai.venice.ui.theme.VeniceVoid
import com.ai.venice.util.ProductVisualEnhancer
import com.ai.venice.util.ResourceUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun UniversalProductImage(
    source: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    if (source.isNullOrBlank()) {
        Box(
            modifier = modifier.background(VeniceSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = VeniceCyan)
        }
        return
    }

    var bitmap by remember(source) { mutableStateOf<Bitmap?>(null) }
    var hasError by remember(source) { mutableStateOf(false) }

    LaunchedEffect(source) {
        withContext(Dispatchers.IO) {
            try {
                if (source.startsWith("content://") || source.startsWith("file://")) {
                    val uri = Uri.parse(source)
                    if (source.startsWith("file://")) {
                        val file = File(uri.path ?: "")
                        if (file.exists()) {
                            bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        } else {
                            hasError = true
                        }
                    } else {
                        context.contentResolver.openInputStream(uri)?.use { stream ->
                            bitmap = BitmapFactory.decodeStream(stream)
                        } ?: run { hasError = true }
                    }
                } else if (source.startsWith("android.resource://")) {
                    val lastSegment = source.substringAfterLast("/")
                    val id = lastSegment.toIntOrNull()
                    if (id != null) {
                        bitmap = BitmapFactory.decodeResource(context.resources, id)
                    } else {
                        hasError = true
                    }
                } else {
                    val resId = ResourceUtils.getDrawableIdByName(context, source)
                    if (resId != 0) {
                        bitmap = BitmapFactory.decodeResource(context.resources, resId)
                    } else {
                        hasError = true
                    }
                }
            } catch (e: Exception) {
                hasError = true
            }
        }
    }

    val currentBitmap = bitmap
    if (currentBitmap != null) {
        Image(
            bitmap = currentBitmap.asImageBitmap(),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    } else if (hasError) {
        val fallbackId = ResourceUtils.getDrawableIdByName(context, "studio_perfume_sample")
        if (fallbackId != 0) {
            Image(
                painter = painterResource(id = fallbackId),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        } else {
            Box(
                modifier = modifier.background(VeniceSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Image, contentDescription = null, tint = VeniceCyan)
            }
        }
    } else {
        Box(
            modifier = modifier.background(VeniceSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = VeniceCyan,
                strokeWidth = 2.dp
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductTransformationShowcaseCard(
    message: ChatMessage,
    onCopyReport: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Studio (Optimisé), 1: Avant (Brut), 2: Côte à Côte
    var selectedChannelRatio by remember { mutableIntStateOf(0) } // 0: 1:1, 1: 9:16, 2: 16:9
    val details = message.transformationDetails

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VeniceSurface)
            .border(1.dp, VeniceBorderLight, RoundedCornerShape(16.dp))
            .padding(14.dp)
            .testTag("product_transformation_card")
    ) {
        // Header Tag Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(VeniceCyan)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "VLAD E-COMMERCE STUDIO",
                    color = VeniceCyan,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    letterSpacing = 0.8.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(VeniceGreen.copy(alpha = 0.15f))
                    .border(1.dp, VeniceGreen.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "CONVERSION : +${details?.conversionScore ?: 98}%",
                    color = VeniceGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Toggle Tabs: Studio / Avant / Comparatif
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = VeniceSurfaceVariant,
            contentColor = VeniceCyan,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = VeniceCyan
                )
            },
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .height(38.dp)
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "✨ Studio (Optimisé)",
                        fontSize = 11.sp,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) VeniceCyan else TextSecondary
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "📷 Avant (Brut)",
                        fontSize = 11.sp,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) VeniceCyan else TextSecondary
                    )
                }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = {
                    Text(
                        text = "⚖️ Côte à côte",
                        fontSize = 11.sp,
                        fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 2) VeniceCyan else TextSecondary
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Visual Display Stage
        val targetAspectRatio = when (selectedChannelRatio) {
            1 -> 9f / 16f
            2 -> 16f / 9f
            else -> 1f
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(VeniceVoid)
                .border(1.dp, VeniceBorder, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> {
                    // Studio Optimized
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(targetAspectRatio)
                    ) {
                        UniversalProductImage(
                            source = message.processedImageUri,
                            contentDescription = "Visuel studio e-commerce optimisé",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        // Floating Studio Badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.Black.copy(alpha = 0.7f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Éclairage 5500K • Zéro Parasite",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                1 -> {
                    // Raw Input (Before)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(targetAspectRatio)
                    ) {
                        UniversalProductImage(
                            source = message.originalImageUri,
                            contentDescription = "Photo brute originale",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(VeniceCoral.copy(alpha = 0.85f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Photo brute non traitée",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                2 -> {
                    // Side-by-side comparison
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                        ) {
                            UniversalProductImage(
                                source = message.originalImageUri,
                                contentDescription = "Avant",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(6.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.Black.copy(alpha = 0.75f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("AVANT (BRUT)", color = VeniceCoral, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .fillMaxSize()
                                .background(VeniceCyan)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                        ) {
                            UniversalProductImage(
                                source = message.processedImageUri,
                                contentDescription = "Après",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(6.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.Black.copy(alpha = 0.75f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("APRÈS (STUDIO)", color = VeniceCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Ratio / Channel format selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val ratios = listOf("1:1 E-Shop", "9:16 Story", "16:9 Bannière")
            ratios.forEachIndexed { index, title ->
                val isSelected = selectedChannelRatio == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) VeniceCyan.copy(alpha = 0.15f) else VeniceSurfaceVariant)
                        .border(1.dp, if (isSelected) VeniceCyan else VeniceBorder, RoundedCornerShape(8.dp))
                        .clickable { selectedChannelRatio = index }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) VeniceCyan else TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Cleaned / Suppressed elements pills
        Text(
            text = "ÉLÉMENTS SUPPRIMÉS & OPTIMISÉS",
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.6.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val items = details?.removedElements ?: listOf(
                "Logo boutique supprimé",
                "Numéro de téléphone effacé",
                "Adresses web/e-mails purgés",
                "Arrière-plan brut nettoyé"
            )
            items.forEach { elem ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(VeniceSurfaceVariant)
                        .border(1.dp, VeniceBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = VeniceCyan,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = elem,
                            color = TextPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons Row (Download & Copy Report)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    Toast.makeText(context, "Visuel e-commerce studio prêt pour téléchargement", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .testTag("download_product_visual_button"),
                colors = ButtonDefaults.buttonColors(containerColor = VeniceCyan),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Télécharger",
                    tint = VeniceVoid,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Télécharger",
                    color = VeniceVoid,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            OutlinedButton(
                onClick = onCopyReport,
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copier le rapport",
                    tint = TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Copier Rapport",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Detailed Marketing Analysis & Strategy Text
        SelectionContainer {
            Text(
                text = message.content,
                color = TextPrimary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }
    }
}

@Composable
fun ProductAttachmentPreview(
    imageUri: String,
    onRemove: () -> Unit,
    onPresetPromptClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(VeniceSurfaceVariant)
            .border(1.dp, VeniceCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UniversalProductImage(
                source = imageUri,
                contentDescription = "Aperçu produit sélectionné",
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, VeniceBorderLight, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = VeniceCyan,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Image Produit Détectée",
                        color = VeniceCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = "Vlad AI va purger les logos/téléphones et appliquer un éclairage studio haute conversion.",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )
            }

            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Supprimer l'image",
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Preset Prompt Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val presets = listOf(
                "Fond Blanc Pur" to "Fond blanc pur studio avec ombre douce portée, sans logo ni téléphone.",
                "Noir & Luxe" to "Ambiance studio noir minimaliste et marbre avec rim-light.",
                "Nature & Bois" to "Décor organique clair en bois naturel pour cosmétiques."
            )
            presets.forEach { (label, prompt) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(VeniceSurfaceContainer)
                        .border(1.dp, VeniceBorder, RoundedCornerShape(8.dp))
                        .clickable { onPresetPromptClick(prompt) }
                        .padding(vertical = 5.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = TextPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDemoSelectorBottomSheet(
    onSelectDemo: (ProductSample) -> Unit,
    onPickFromGallery: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = VeniceSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Studio E-Commerce Vlad AI",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted)
                }
            }

            Text(
                text = "Téléversez une image de produit ou essayez un exemple avec logos et parasites pour constater la transformation instantanée.",
                color = TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            // Button to open device gallery
            Button(
                onClick = {
                    onDismiss()
                    onPickFromGallery()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("pick_custom_product_photo_button"),
                colors = ButtonDefaults.buttonColors(containerColor = VeniceCyan),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = VeniceVoid,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Importer ma photo de produit (Galerie)",
                    color = VeniceVoid,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "OU TESTER AVEC UN ÉCHANTILLON BRUT",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProductVisualEnhancer.productSamples.forEach { sample ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(VeniceSurfaceVariant)
                        .border(1.dp, VeniceBorder, RoundedCornerShape(12.dp))
                        .clickable {
                            onDismiss()
                            onSelectDemo(sample)
                        }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UniversalProductImage(
                        source = sample.rawDrawableName,
                        contentDescription = sample.title,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, VeniceBorderLight, RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = sample.title,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Contient logo boutique, n° téléphone & arrière-plan encombré",
                            color = VeniceCoral,
                            fontSize = 10.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Transformer",
                        tint = VeniceCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ProductEcommerceHeroBanner(
    onSelectDemo: (ProductSample) -> Unit,
    onPickFromGallery: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        VeniceSurfaceContainer,
                        VeniceSurfaceVariant
                    )
                )
            )
            .border(1.dp, VeniceCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(VeniceCyan.copy(alpha = 0.15f))
                    .border(1.dp, VeniceCyan.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = null,
                    tint = VeniceCyan,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = "Studio E-Commerce & Visuels Produits",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "Purge des logos & numéros • Fond blanc/décor • Éclairage 5500K",
                    color = VeniceCyan,
                    fontSize = 10.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Envoyez n'importe quelle photo de produit brute. Vlad AI élimine les éléments parasites (logos de magasin, numéros, emails, bazar) et génère un visuel studio prêt à convertir.",
            color = TextSecondary,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onPickFromGallery,
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VeniceCyan),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = VeniceVoid,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Ma Photo",
                    color = VeniceVoid,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            OutlinedButton(
                onClick = {
                    val demo = ProductVisualEnhancer.productSamples.first()
                    onSelectDemo(demo)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = VeniceCyan)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = VeniceCyan,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Démo Parfum",
                    color = VeniceCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }
    }
}
