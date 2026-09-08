package com.ai.venice.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.venice.model.VeniceModel
import com.ai.venice.model.VeniceSettings
import com.ai.venice.ui.theme.TextMuted
import com.ai.venice.ui.theme.TextPrimary
import com.ai.venice.ui.theme.TextSecondary
import com.ai.venice.ui.theme.VeniceBorder
import com.ai.venice.ui.theme.VeniceBorderLight
import com.ai.venice.ui.theme.VeniceCoral
import com.ai.venice.ui.theme.VeniceCyan
import com.ai.venice.ui.theme.VeniceGreen
import com.ai.venice.ui.theme.VeniceSurface
import com.ai.venice.ui.theme.VeniceSurfaceVariant
import com.ai.venice.ui.theme.VeniceVoid
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    settings: VeniceSettings,
    allModels: List<VeniceModel>,
    onUpdateSettings: (VeniceSettings) -> Unit,
    onToggleWallet: () -> Unit
) {
    val context = LocalContext.current
    var apiKeyText by remember { mutableStateOf(settings.apiKey) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(VeniceVoid),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "VLAD SYSTEM",
                    color = VeniceCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Sovereignty & Privacy",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Decentralized architecture with verifiable client-side privacy controls.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // Sovereignty Guarantees Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VeniceSurface)
                    .border(1.dp, VeniceBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(VeniceGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = VeniceGreen, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "ZERO-LOG RETENTION", color = VeniceGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
                        Text(text = "End-to-End Privacy Active", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Zero logging switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Zero Telemetry & Prompts", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "Prompts are computed in volatile RAM and immediately purged.", color = TextMuted, fontSize = 11.sp)
                    }
                    Switch(
                        checked = settings.zeroLogging,
                        onCheckedChange = { onUpdateSettings(settings.copy(zeroLogging = it)) },
                        colors = SwitchDefaults.colors(checkedThumbColor = VeniceVoid, checkedTrackColor = VeniceGreen)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Uncensored Mode switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Uncensored Intelligence", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "Bypasses corporate censorship and moralized refusal filters.", color = TextMuted, fontSize = 11.sp)
                    }
                    Switch(
                        checked = settings.uncensoredMode,
                        onCheckedChange = { onUpdateSettings(settings.copy(uncensoredMode = it)) },
                        colors = SwitchDefaults.colors(checkedThumbColor = VeniceVoid, checkedTrackColor = VeniceCoral)
                    )
                }
            }
        }

        // Web3 / Sovereign Wallet Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VeniceSurface)
                    .border(1.dp, VeniceBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AccountBalanceWallet, contentDescription = null, tint = VeniceCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "WEB3 SOVEREIGN IDENTITY", color = VeniceCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (settings.connectedWallet != null)
                        "Connected: ${settings.connectedWallet}"
                    else
                        "Connect your Web3 wallet (Metamask, Phantom, Coinbase) for decentralized compute access.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onToggleWallet,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (settings.connectedWallet != null) VeniceSurfaceVariant else VeniceCyan
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (settings.connectedWallet != null) "Disconnect Wallet" else "Connect Web3 Wallet",
                        color = if (settings.connectedWallet != null) VeniceCoral else VeniceVoid,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // API Key Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VeniceSurface)
                    .border(1.dp, VeniceBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = VeniceCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "VLAD API CREDENTIALS", color = VeniceCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Optionally supply your custom Vlad AI API token or use the built-in sovereign node.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = apiKeyText,
                    onValueChange = { apiKeyText = it },
                    placeholder = { Text("vlad_live_...", color = TextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = VeniceVoid,
                        unfocusedContainerColor = VeniceVoid,
                        focusedBorderColor = VeniceCyan,
                        unfocusedBorderColor = VeniceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        onUpdateSettings(settings.copy(apiKey = apiKeyText.trim()))
                        Toast.makeText(context, "API Key Saved", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VeniceSurfaceVariant),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = VeniceCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Key", color = VeniceCyan, fontSize = 12.sp)
                }
            }
        }

        // Inference Tuning (Temperature)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VeniceSurface)
                    .border(1.dp, VeniceBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Tune, contentDescription = null, tint = VeniceCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "INFERENCE TEMPERATURE", color = VeniceCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Creativity & Randomness", color = TextPrimary, fontSize = 13.sp)
                    Text(
                        text = String.format("%.2f", settings.temperature),
                        color = VeniceCyan,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Slider(
                    value = settings.temperature,
                    onValueChange = { onUpdateSettings(settings.copy(temperature = it)) },
                    valueRange = 0.1f..1.5f,
                    colors = SliderDefaults.colors(
                        thumbColor = VeniceCyan,
                        activeTrackColor = VeniceCyan,
                        inactiveTrackColor = VeniceSurfaceVariant
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Precise (0.1)", color = TextMuted, fontSize = 11.sp)
                    Text("Balanced (0.7)", color = TextMuted, fontSize = 11.sp)
                    Text("Creative (1.5)", color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        // About Info
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VeniceSurface)
                    .border(1.dp, VeniceBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = com.ai.venice.R.drawable.vlad_ai_logo),
                    contentDescription = "Vlad AI Logo",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(2.dp, VeniceCyan, CircleShape),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "VLAD AI", color = TextPrimary, fontWeight = FontWeight.Black, fontSize = 18.sp, letterSpacing = 2.sp)
                Text(text = "Version 1.0.0 (Native Android Edition)", color = TextSecondary, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
                Text(text = "Built with Kotlin & Jetpack Compose", color = TextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(VeniceSurfaceVariant)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "Encrypted • Uncensored • Sovereign",
                        color = VeniceCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
