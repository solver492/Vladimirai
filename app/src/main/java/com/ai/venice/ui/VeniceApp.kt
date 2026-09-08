package com.ai.venice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ai.venice.ui.components.VeniceBottomBar
import com.ai.venice.ui.components.VeniceTopBar
import com.ai.venice.ui.screens.ChatScreen
import com.ai.venice.ui.screens.HistoryScreen
import com.ai.venice.ui.screens.ImageStudioScreen
import com.ai.venice.ui.screens.MindsScreen
import com.ai.venice.ui.screens.SettingsScreen
import com.ai.venice.ui.theme.VeniceVoid

@Composable
fun VeniceApp(
    viewModel: VeniceViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val messages by viewModel.currentMessages.collectAsState()
    val selectedModel by viewModel.selectedModel.collectAsState()
    val selectedMind by viewModel.selectedMind.collectAsState()
    val minds by viewModel.minds.collectAsState()
    val sessions by viewModel.sessions.collectAsState()
    val activeSessionId by viewModel.activeSessionId.collectAsState()
    val generatedArts by viewModel.generatedArts.collectAsState()
    val selectedPreset by viewModel.selectedPreset.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val isGeneratingResponse by viewModel.isGeneratingResponse.collectAsState()
    val isGeneratingArt by viewModel.isGeneratingArt.collectAsState()

    Scaffold(
        topBar = {
            VeniceTopBar(
                currentTab = currentTab,
                onNewChatClick = { viewModel.startNewChat() }
            )
        },
        bottomBar = {
            VeniceBottomBar(
                currentTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        },
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        containerColor = VeniceVoid
    ) { paddingValues ->
        val context = androidx.compose.ui.platform.LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VeniceVoid)
                .padding(paddingValues)
        ) {
            when (currentTab) {
                "chat" -> ChatScreen(
                    messages = messages,
                    selectedModel = selectedModel,
                    selectedMind = selectedMind,
                    allModels = viewModel.models,
                    allMinds = minds,
                    isGenerating = isGeneratingResponse,
                    onModelSelected = { viewModel.setModel(it) },
                    onMindSelected = { viewModel.setMind(it) },
                    onSendMessage = { text, imageUri -> viewModel.sendMessage(text, imageUri, context) },
                    onNavigateToMinds = { viewModel.selectTab("minds") }
                )
                "images", "studio" -> ImageStudioScreen(
                    presets = viewModel.imagePresets,
                    selectedPreset = selectedPreset,
                    generatedArts = generatedArts,
                    isGenerating = isGeneratingArt,
                    onPresetSelected = { viewModel.setSelectedPreset(it) },
                    onGenerate = { prompt, preset, ratio ->
                        viewModel.generateArt(prompt, preset, ratio)
                    },
                    onDeleteArt = { viewModel.deleteArt(it) }
                )
                "minds" -> MindsScreen(
                    minds = minds,
                    onSelectMindAndChat = { mind ->
                        viewModel.startNewChat(mind = mind)
                    },
                    onCreateCustomMind = { name, tagline, prompt, graphic ->
                        viewModel.addCustomMind(name, tagline, prompt, graphic)
                    }
                )
                "history" -> HistoryScreen(
                    sessions = sessions,
                    minds = minds,
                    activeSessionId = activeSessionId,
                    onSelectSession = { viewModel.selectSession(it) },
                    onDeleteSession = { viewModel.deleteSession(it) },
                    onStartNewChat = { viewModel.startNewChat() }
                )
                "settings" -> SettingsScreen(
                    settings = settings,
                    allModels = viewModel.models,
                    onUpdateSettings = { viewModel.updateSettings(it) },
                    onToggleWallet = { viewModel.toggleWallet() }
                )
                else -> {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    ChatScreen(
                        messages = messages,
                        selectedModel = selectedModel,
                        selectedMind = selectedMind,
                        allModels = viewModel.models,
                        allMinds = minds,
                        isGenerating = isGeneratingResponse,
                        onModelSelected = { viewModel.setModel(it) },
                        onMindSelected = { viewModel.setMind(it) },
                        onSendMessage = { text, imageUri -> viewModel.sendMessage(text, imageUri, context) },
                        onNavigateToMinds = { viewModel.selectTab("minds") }
                    )
                }
            }
        }
    }
}
