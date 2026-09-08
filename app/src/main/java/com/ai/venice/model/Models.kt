package com.ai.venice.model

data class VeniceModel(
    val id: String,
    val name: String,
    val description: String,
    val badge: String,
    val isUncensored: Boolean = false,
    val speed: String = "Fast"
)

data class Mind(
    val id: String,
    val name: String,
    val tagLine: String,
    val category: String,
    val systemPrompt: String,
    val graphicDrawableName: String = "assets_images_minds_mindsemptystategraphic_01",
    val isCustom: Boolean = false,
    val recommendedModelId: String = "llama-3.3-70b-uncensored"
)

data class ChatMessage(
    val id: String,
    val chatId: String,
    val role: String, // "user" or "assistant"
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val modelName: String = "",
    val mindName: String = ""
)

data class ChatSession(
    val id: String,
    val title: String,
    val mindId: String,
    val modelId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastMessage: String = ""
)

data class ImageStylePreset(
    val id: String,
    val name: String,
    val description: String,
    val promptSuffix: String,
    val negativePrompt: String,
    val drawableName: String
)

data class GeneratedArt(
    val id: String,
    val prompt: String,
    val styleName: String,
    val aspectRatio: String,
    val timestamp: Long = System.currentTimeMillis(),
    val drawableName: String? = null
)

data class VeniceSettings(
    val apiKey: String = "",
    val zeroLogging: Boolean = true,
    val uncensoredMode: Boolean = true,
    val defaultModelId: String = "llama-3.3-70b-uncensored",
    val temperature: Float = 0.7f,
    val webSearchEnabled: Boolean = false,
    val connectedWallet: String? = null,
    val voice: String = "Ara"
)
