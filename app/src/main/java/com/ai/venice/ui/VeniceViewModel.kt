package com.ai.venice.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.venice.data.VeniceRepository
import com.ai.venice.model.ChatMessage
import com.ai.venice.model.ChatSession
import com.ai.venice.model.GeneratedArt
import com.ai.venice.model.ImageStylePreset
import com.ai.venice.model.Mind
import com.ai.venice.model.VeniceModel
import com.ai.venice.model.VeniceSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VeniceViewModel : ViewModel() {

    private val repository = VeniceRepository

    private val _currentTab = MutableStateFlow("chat")
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    val minds: StateFlow<List<Mind>> = repository.minds
    val sessions: StateFlow<List<ChatSession>> = repository.sessions
    val generatedArts: StateFlow<List<GeneratedArt>> = repository.generatedArts
    val settings: StateFlow<VeniceSettings> = repository.settings
    val models: List<VeniceModel> = repository.availableModels
    val imagePresets: List<ImageStylePreset> = repository.imagePresets

    private val _activeSessionId = MutableStateFlow("welcome_session")
    val activeSessionId: StateFlow<String> = _activeSessionId.asStateFlow()

    private val _selectedModel = MutableStateFlow(models.first())
    val selectedModel: StateFlow<VeniceModel> = _selectedModel.asStateFlow()

    private val _selectedMind = MutableStateFlow(repository.minds.value.first())
    val selectedMind: StateFlow<Mind> = _selectedMind.asStateFlow()

    private val _selectedPreset = MutableStateFlow(
        imagePresets.firstOrNull() ?: ImageStylePreset(
            id = "preset_default",
            name = "Studio",
            description = "High quality generative synthesis",
            promptSuffix = ", ultra high detail, 8k",
            negativePrompt = "blurry, low quality",
            drawableName = "vlad_ai_logo"
        )
    )
    val selectedPreset: StateFlow<ImageStylePreset> = _selectedPreset.asStateFlow()

    private val _isGeneratingResponse = MutableStateFlow(false)
    val isGeneratingResponse: StateFlow<Boolean> = _isGeneratingResponse.asStateFlow()

    private val _isGeneratingArt = MutableStateFlow(false)
    val isGeneratingArt: StateFlow<Boolean> = _isGeneratingArt.asStateFlow()

    val currentMessages: StateFlow<List<ChatMessage>> = combine(
        repository.messages,
        _activeSessionId
    ) { allMessages, activeId ->
        allMessages[activeId] ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: String) {
        _currentTab.value = if (tab == "images") "studio" else tab
    }

    fun selectSession(sessionId: String) {
        _activeSessionId.value = sessionId
        val session = sessions.value.find { it.id == sessionId }
        if (session != null) {
            val mind = minds.value.find { it.id == session.mindId }
            if (mind != null) _selectedMind.value = mind
            val model = models.find { it.id == session.modelId }
            if (model != null) _selectedModel.value = model
        }
        _currentTab.value = "chat"
    }

    fun startNewChat(mind: Mind? = null, model: VeniceModel? = null) {
        val targetMind = mind ?: _selectedMind.value
        val targetModel = model ?: _selectedModel.value
        val newId = repository.createChatSession(targetMind.id, targetModel.id)
        _selectedMind.value = targetMind
        _selectedModel.value = targetModel
        _activeSessionId.value = newId
        _currentTab.value = "chat"
    }

    fun deleteSession(sessionId: String) {
        repository.deleteSession(sessionId)
        if (_activeSessionId.value == sessionId) {
            val remaining = sessions.value
            if (remaining.isNotEmpty()) {
                _activeSessionId.value = remaining.first().id
            } else {
                startNewChat()
            }
        }
    }

    fun setModel(model: VeniceModel) {
        _selectedModel.value = model
    }

    fun setMind(mind: Mind) {
        _selectedMind.value = mind
    }

    fun setSelectedPreset(preset: ImageStylePreset) {
        _selectedPreset.value = preset
    }

    fun sendMessage(text: String, imageUri: String? = null, context: android.content.Context? = null) {
        if ((text.isBlank() && imageUri == null) || _isGeneratingResponse.value) return
        val currentChatId = _activeSessionId.value

        viewModelScope.launch {
            _isGeneratingResponse.value = true
            try {
                repository.sendMessage(
                    chatId = currentChatId,
                    userText = text.trim(),
                    imageUri = imageUri,
                    context = context
                )
            } finally {
                _isGeneratingResponse.value = false
            }
        }
    }

    fun generateArt(prompt: String, preset: ImageStylePreset, aspectRatio: String) {
        if (prompt.isBlank() || _isGeneratingArt.value) return

        viewModelScope.launch {
            _isGeneratingArt.value = true
            try {
                delay(1200) // Realistic studio generation cycle
                repository.generateArt(prompt.trim(), preset, aspectRatio)
            } finally {
                _isGeneratingArt.value = false
            }
        }
    }

    fun deleteArt(artId: String) {
        repository.deleteArt(artId)
    }

    fun addCustomMind(name: String, tagLine: String, systemPrompt: String, graphicName: String) {
        repository.addCustomMind(name, tagLine, systemPrompt, graphicName)
    }

    fun updateSettings(newSettings: VeniceSettings) {
        repository.updateSettings(newSettings)
    }

    fun toggleWallet() {
        repository.toggleWalletConnection()
    }
}
