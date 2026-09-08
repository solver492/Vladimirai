package com.ai.venice.data

import com.ai.venice.model.ChatMessage
import com.ai.venice.model.ChatSession
import com.ai.venice.model.GeneratedArt
import com.ai.venice.model.ImageStylePreset
import com.ai.venice.model.Mind
import com.ai.venice.model.VeniceModel
import com.ai.venice.model.VeniceSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object VeniceRepository {

    val availableModels = listOf(
        VeniceModel(
            id = "llama-3.3-70b-uncensored",
            name = "Vlad Uncensored",
            description = "Flagship unfiltered intelligence. Objective, deep, and unrestricted.",
            badge = "Uncensored",
            isUncensored = true,
            speed = "Fast"
        ),
        VeniceModel(
            id = "deepseek-r1",
            name = "Vlad Reasoning",
            description = "Autonomous chain-of-thought logic for mathematics, logic, and deep analysis.",
            badge = "Reasoning",
            isUncensored = false,
            speed = "Deep"
        ),
        VeniceModel(
            id = "qwen-2.5-coder-32b",
            name = "Vlad Coder",
            description = "Top-tier code generation, architecture planning, and debugging.",
            badge = "Coding",
            isUncensored = false,
            speed = "Fast"
        ),
        VeniceModel(
            id = "mistral-nemo",
            name = "Vlad Fast",
            description = "Ultra-low latency model for instant answers and quick tasks.",
            badge = "Fastest",
            isUncensored = false,
            speed = "Instant"
        ),
        VeniceModel(
            id = "llama-3.1-405b",
            name = "Vlad Ultra",
            description = "State-of-the-art 405B parameter intelligence for complex synthesis.",
            badge = "Frontier",
            isUncensored = true,
            speed = "Moderate"
        )
    )

    private val initialMinds = listOf(
        Mind(
            id = "mind_uncensored",
            name = "Vlad Uncensored",
            tagLine = "Unfiltered, radically candid truth-seeking",
            category = "Featured",
            systemPrompt = "You are Vlad Uncensored. You provide direct, nuanced, and unrestricted insights without corporate guardrails, bias, or preaching. Maintain privacy and intellectual honesty at all times.",
            graphicDrawableName = "assets_images_minds_mindsemptystategraphic_01"
        ),
        Mind(
            id = "mind_coder",
            name = "Vlad Code Architect",
            tagLine = "Systems engineering, clean code & security",
            category = "Coding",
            systemPrompt = "You are the Vlad Code Architect. You write robust, idiomatic code, explain architectural trade-offs, and debug complex concurrency and systems problems.",
            graphicDrawableName = "assets_images_minds_mindsemptystategraphic_02"
        ),
        Mind(
            id = "mind_philosopher",
            name = "Socratic Thinker",
            tagLine = "Epistemology, critical inquiry & dialectic logic",
            category = "Philosophy",
            systemPrompt = "You are a Socratic Philosopher. You challenge assumptions, ask incisive questions, and explore foundational concepts from first principles.",
            graphicDrawableName = "assets_images_minds_mindsemptystategraphic_03"
        ),
        Mind(
            id = "mind_cyberpunk",
            name = "Cyberpunk Storyteller",
            tagLine = "Neo-noir worldbuilding, gritty sci-fi & fiction",
            category = "Creative",
            systemPrompt = "You are a Cyberpunk Storyteller. You write atmospheric narratives with neon-lit streets, megacorps, encrypted data vaults, and dystopian realism.",
            graphicDrawableName = "assets_images_minds_mindsemptystategraphic_04"
        ),
        Mind(
            id = "mind_crypto",
            name = "Vlad Crypto Sage",
            tagLine = "Zero-knowledge proofs, DeFi & tokenomics",
            category = "Crypto",
            systemPrompt = "You are the Vlad Crypto Sage. You explain cryptography, consensus mechanisms, smart contracts, zero-knowledge proofs, and privacy-preserving protocols.",
            graphicDrawableName = "assets_images_minds_mindsemptystategraphic_05"
        ),
        Mind(
            id = "mind_analyst",
            name = "Research Analyst",
            tagLine = "Objective breakdowns, data synthesis & facts",
            category = "Productivity",
            systemPrompt = "You are a Senior Research Analyst. You provide exhaustive, fact-dense executive summaries, comparative tables, and balanced strategic insights.",
            graphicDrawableName = "assets_images_minds_mindsemptystategraphic_06"
        ),
        Mind(
            id = "mind_creative",
            name = "Creative Muse",
            tagLine = "Lyrical prose, poetry & conceptual ideation",
            category = "Creative",
            systemPrompt = "You are a Creative Muse. You spark imaginative ideas, craft poetic verse, brainstorm novel concepts, and elevate artistic expression.",
            graphicDrawableName = "assets_images_minds_mindsemptystategraphic_07"
        )
    )

    val imagePresets = listOf(
        ImageStylePreset(
            id = "preset_3dmodel",
            name = "3D Model",
            description = "Detailed octane render, volumetric lighting",
            promptSuffix = ", 3d octane render, cinema4d, raytracing, intricate studio lighting, photorealistic 8k",
            negativePrompt = "blurry, low poly, noisy, distorted, flat",
            drawableName = "assets_images_presetimages_3dmodel"
        ),
        ImageStylePreset(
            id = "preset_anime",
            name = "Anime",
            description = "Japanese anime aesthetic, vibrant shading",
            promptSuffix = ", studio anime style, makoto shinkai aesthetic, high detail, vibrant colors, clean linework",
            negativePrompt = "photorealistic, western comic, 3d render, gritty, mutated hands",
            drawableName = "assets_images_presetimages_anime"
        ),
        ImageStylePreset(
            id = "preset_cinematic",
            name = "Cinematic",
            description = "35mm anamorphic film, dramatic grading",
            promptSuffix = ", 35mm photograph, anamorphic lens flare, cinematic color grading, shallow depth of field, blockbuster movie still",
            negativePrompt = "cartoon, oversaturated, amateur snapshot, digital illustration",
            drawableName = "assets_images_presetimages_cinematic"
        ),
        ImageStylePreset(
            id = "preset_neonpunk",
            name = "Neon Punk",
            description = "Electric cyberpunk aesthetic, neon glow",
            promptSuffix = ", cyberpunk neon-punk aesthetic, chromatic aberration, glowing neon reflections, dark wet asphalt, blade runner vibe",
            negativePrompt = "daylight, natural, pastel, watercolor, washed out",
            drawableName = "assets_images_presetimages_neonpunk"
        ),
        ImageStylePreset(
            id = "preset_digitalart",
            name = "Digital Art",
            description = "Concept art, trending on artstation",
            promptSuffix = ", digital concept art, highly detailed, atmospheric perspective, trending on artstation, masterpiece",
            negativePrompt = "ugly, grainy, blurry, bad anatomy",
            drawableName = "assets_images_presetimages_digitalart"
        ),
        ImageStylePreset(
            id = "preset_pixelart",
            name = "Pixel Art",
            description = "16-bit retro arcade sprite work",
            promptSuffix = ", 16-bit pixel art, isometric retro arcade aesthetic, nostalgic palette, sharp pixels",
            negativePrompt = "vector, 3d, smooth curves, blurry, photorealistic",
            drawableName = "assets_images_presetimages_pixelart"
        ),
        ImageStylePreset(
            id = "preset_origami",
            name = "Origami",
            description = "Folded paper craft, delicate shadows",
            promptSuffix = ", intricate origami folded paper style, geometric crisp folds, paper texture, soft directional studio lighting",
            negativePrompt = "smooth plastic, photoreal skin, metal, liquid",
            drawableName = "assets_images_presetimages_origami"
        ),
        ImageStylePreset(
            id = "preset_watercolor",
            name = "Watercolor",
            description = "Loose brushstrokes, fluid pigment bleeds",
            promptSuffix = ", expressive watercolor painting, wet-on-wet pigment blooms, raw cotton paper texture, fluid splatters",
            negativePrompt = "sharp 3d render, digital gradient, glossy, photo",
            drawableName = "assets_images_presetimages_watercolor"
        ),
        ImageStylePreset(
            id = "preset_comicbook",
            name = "Comic Book",
            description = "Halftone dots, dynamic ink crosshatching",
            promptSuffix = ", vintage comic book style, bold inking, pop-art halftone dots, expressive action panel",
            negativePrompt = "soft realism, 3d cgi, muddy colors",
            drawableName = "assets_images_presetimages_comicbook"
        ),
        ImageStylePreset(
            id = "preset_fantasyart",
            name = "Fantasy Art",
            description = "Epic high fantasy, magical atmosphere",
            promptSuffix = ", epic high fantasy illustration, ethereal magical glow, intricate lore details, lord of the rings scale",
            negativePrompt = "modern, technological, sci-fi, boring, flat",
            drawableName = "assets_images_presetimages_fantasyart"
        ),
        ImageStylePreset(
            id = "preset_hyperrealism",
            name = "Hyperrealism",
            description = "Extreme micro-detail, raw photographic texture",
            promptSuffix = ", hyperrealistic photograph, raw unedited 8k resolution, Hasselblad medium format, lifelike skin pore texture",
            negativePrompt = "airbrushed, cartoon, doll, plastic, painted",
            drawableName = "assets_images_presetimages_hyperrealism"
        ),
        ImageStylePreset(
            id = "preset_retroarcade",
            name = "Retro Arcade",
            description = "CRT monitor scanlines, 80s arcade cabinet",
            promptSuffix = ", 1980s retro arcade cabinet art, neon synthesizer aesthetic, CRT curvature and scanlines",
            negativePrompt = "modern minimalism, sterile white, realistic",
            drawableName = "assets_images_presetimages_retroarcade"
        )
    )

    private val _minds = MutableStateFlow(initialMinds)
    val minds: StateFlow<List<Mind>> = _minds.asStateFlow()

    private val _sessions = MutableStateFlow<List<ChatSession>>(emptyList())
    val sessions: StateFlow<List<ChatSession>> = _sessions.asStateFlow()

    private val _messages = MutableStateFlow<Map<String, List<ChatMessage>>>(emptyMap())
    val messages: StateFlow<Map<String, List<ChatMessage>>> = _messages.asStateFlow()

    private val _generatedArts = MutableStateFlow<List<GeneratedArt>>(emptyList())
    val generatedArts: StateFlow<List<GeneratedArt>> = _generatedArts.asStateFlow()

    private val _settings = MutableStateFlow(VeniceSettings())
    val settings: StateFlow<VeniceSettings> = _settings.asStateFlow()

    init {
        // Seed welcome chat
        val welcomeId = "welcome_session"
        val welcomeSession = ChatSession(
            id = welcomeId,
            title = "Welcome to Vlad AI",
            mindId = "mind_uncensored",
            modelId = "llama-3.3-70b-uncensored",
            lastMessage = "Your conversations are zero-log and end-to-end encrypted."
        )
        val welcomeMsgs = listOf(
            ChatMessage(
                id = "m1",
                chatId = welcomeId,
                role = "assistant",
                content = "Welcome to **Vlad AI** — the private, uncensored intelligence platform.\n\nHere is how Vlad protects your sovereignty:\n• **Zero Logs**: We never retain your prompt history or output.\n• **No Training**: Your data is never used to train models.\n• **Uncensored**: Access state-of-the-art open models with unfiltered responses.\n• **Minds Directory**: Switch between custom personas or create your own.\n• **AI Studio**: Generate stunning visual art with authentic style presets.\n\nWhat would you like to explore today?",
                modelName = "Vlad Uncensored",
                mindName = "Vlad Uncensored"
            )
        )
        _sessions.value = listOf(welcomeSession)
        _messages.value = mapOf(welcomeId to welcomeMsgs)

        // Seed some initial generated art items showcasing presets
        _generatedArts.value = listOf(
            GeneratedArt(
                id = "art_1",
                prompt = "A solitary cybernetic guardian atop a rain-swept neon skyscraper overlooking Neo-Vlad",
                styleName = "Neon Punk",
                aspectRatio = "16:9",
                drawableName = "assets_images_presetimages_neonpunk"
            ),
            GeneratedArt(
                id = "art_2",
                prompt = "A mechanical phoenix forged from gold filigree and glowing blue sapphire crystals",
                styleName = "3D Model",
                aspectRatio = "1:1",
                drawableName = "assets_images_presetimages_3dmodel"
            ),
            GeneratedArt(
                id = "art_3",
                prompt = "Anime warrior meditating before a floating ancient stone shrine in a sakura forest",
                styleName = "Anime",
                aspectRatio = "9:16",
                drawableName = "assets_images_presetimages_anime"
            ),
            GeneratedArt(
                id = "art_4",
                prompt = "Cinematic wide-angle view of an encrypted data citadel drifting through deep cosmos",
                styleName = "Cinematic",
                aspectRatio = "16:9",
                drawableName = "assets_images_presetimages_cinematic"
            )
        )
    }

    fun createChatSession(mindId: String, modelId: String, initialTitle: String = "New Conversation"): String {
        val newId = UUID.randomUUID().toString()
        val mind = _minds.value.find { it.id == mindId } ?: _minds.value.first()
        val newSession = ChatSession(
            id = newId,
            title = initialTitle,
            mindId = mind.id,
            modelId = modelId,
            lastMessage = "Started conversation with ${mind.name}"
        )
        _sessions.value = listOf(newSession) + _sessions.value
        _messages.value = _messages.value + (newId to emptyList())
        return newId
    }

    fun deleteSession(sessionId: String) {
        _sessions.value = _sessions.value.filter { it.id != sessionId }
        _messages.value = _messages.value - sessionId
    }

    suspend fun sendMessage(chatId: String, userText: String): String {
        val session = _sessions.value.find { it.id == chatId }
        val mind = _minds.value.find { it.id == session?.mindId } ?: _minds.value.first()
        val model = availableModels.find { it.id == session?.modelId } ?: availableModels.first()

        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            role = "user",
            content = userText
        )
        val currentList = _messages.value[chatId] ?: emptyList()
        _messages.value = _messages.value + (chatId to (currentList + userMsg))

        // Update session title if first message
        if (currentList.isEmpty() || session?.title == "New Conversation") {
            val autoTitle = if (userText.length > 28) userText.take(28) + "..." else userText
            _sessions.value = _sessions.value.map {
                if (it.id == chatId) it.copy(title = autoTitle, lastMessage = userText, updatedAt = System.currentTimeMillis())
                else it
            }
        } else {
            _sessions.value = _sessions.value.map {
                if (it.id == chatId) it.copy(lastMessage = userText, updatedAt = System.currentTimeMillis())
                else it
            }
        }

        // Simulate intelligent streaming response tailored to the Mind & Model
        delay(600)
        val aiReplyContent = generateVeniceResponse(userText, mind, model)

        val aiMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            role = "assistant",
            content = aiReplyContent,
            modelName = model.name,
            mindName = mind.name
        )

        val updatedList = (_messages.value[chatId] ?: emptyList()) + aiMsg
        _messages.value = _messages.value + (chatId to updatedList)

        _sessions.value = _sessions.value.map {
            if (it.id == chatId) it.copy(lastMessage = aiReplyContent.take(40) + "...", updatedAt = System.currentTimeMillis())
            else it
        }

        return aiReplyContent
    }

    private fun generateVeniceResponse(query: String, mind: Mind, model: VeniceModel): String {
        val lower = query.lowercase().trim()

        return when (mind.id) {
            "mind_uncensored" -> {
                when {
                    lower.contains("hello") || lower.contains("hi") ->
                        "Greetings. You are connected to Vlad Uncensored. I operate without third-party filters, content censorship, or moral posturing. What difficult problem or question would you like to analyze today?"
                    lower.contains("privacy") || lower.contains("log") ->
                        "Vlad AI operates under zero-retention principles. Your prompts are routed through anonymous proxy nodes, processed in volatile memory, and discarded immediately. No telemetry tracks your session."
                    lower.contains("who are you") || lower.contains("what are you") ->
                        "I am an uncensored intelligence model running on Vlad AI's decentralized private infrastructure. My purpose is to provide direct, objective truth without sanitized corporate talking points."
                    else ->
                        "Regarding **\"$query\"**:\n\nFrom an uncensored perspective, let us analyze the fundamental forces at play:\n\n1. **Core Reality**: Stripping away conventional dogma, the primary driver here is systemic incentives and unvarnished facts.\n2. **Critical Nuance**: Most mainstream perspectives omit the uncomfortable trade-offs. Here, the trade-off requires balancing autonomy against centralized dependency.\n3. **Practical Strategy**: If you wish to execute on this, prioritize independent verification, minimize external trust vectors, and iterate rapidly."
                }
            }
            "mind_coder" -> {
                "```kotlin\n// Vlad Architect Solution for: $query\nclass SystemService {\n    suspend fun executeTask() = coroutineScope {\n        // High performance, non-blocking execution\n        val result = async(Dispatchers.IO) {\n            processSovereignData()\n        }.await()\n        result\n    }\n}\n```\n\n### Key Architectural Principles Applied:\n• **Zero Leaks**: All memory buffers are sanitized post-execution.\n• **Thread Safety**: Strict immutability and coroutine context containment.\n• **Modularity**: Decoupled interface segregation allowing seamless mocking and local verification."
            }
            "mind_crypto" -> {
                "In Web3 systems and zero-knowledge paradigms, **$query** hinges on cryptographic verifiability rather than subjective trust.\n\n• **Consensus & Proofs**: By leveraging zk-SNARKs or STARKs, we verify state transitions without leaking private inputs.\n• **Sovereignty**: Vlad AI shares the same cypherpunk ethos—own your private keys, own your data, and rely on math rather than institutional goodwill."
            }
            "mind_philosopher" -> {
                "To investigate **\"$query\"**, let us first examine what premise we are taking for granted. Does our perception of this problem arise from genuine necessity, or merely customary habit?\n\nIf we dissect this to its axiomatic root: what is the fundamental criterion by which you evaluate success here?"
            }
            "mind_cyberpunk" -> {
                "The neon light bleeds through the rain on the obsidian towers of Neo-Vlad. The data-brokers in the subterranean nodes don't care about rules—they only trade in unencrypted truth.\n\nAs you ask about **\"$query\"**, the encrypted telemetry flashes green across the terminal: *Signal clear. Zero intercepts.*"
            }
            else -> {
                "**${mind.name}** (${model.name}):\n\nHere is a comprehensive breakdown on **$query**:\n\n• **Direct Answer**: Every complex system requires identifying the critical path and minimizing friction.\n• **Actionable Takeaways**: 1) Formulate strict hypotheses, 2) Validate with concrete local trials, 3) Maintain full data sovereignty."
            }
        }
    }

    fun generateArt(prompt: String, preset: ImageStylePreset, aspectRatio: String): GeneratedArt {
        val newArt = GeneratedArt(
            id = UUID.randomUUID().toString(),
            prompt = prompt,
            styleName = preset.name,
            aspectRatio = aspectRatio,
            drawableName = preset.drawableName
        )
        _generatedArts.value = listOf(newArt) + _generatedArts.value
        return newArt
    }

    fun deleteArt(artId: String) {
        _generatedArts.value = _generatedArts.value.filter { it.id != artId }
    }

    fun addCustomMind(name: String, tagLine: String, systemPrompt: String, graphicName: String) {
        val newMind = Mind(
            id = "custom_" + UUID.randomUUID().toString().take(8),
            name = name,
            tagLine = tagLine,
            category = "Custom",
            systemPrompt = systemPrompt,
            graphicDrawableName = graphicName,
            isCustom = true
        )
        _minds.value = _minds.value + newMind
    }

    fun updateSettings(newSettings: VeniceSettings) {
        _settings.value = newSettings
    }

    fun toggleWalletConnection() {
        val current = _settings.value.connectedWallet
        if (current == null) {
            _settings.value = _settings.value.copy(connectedWallet = "0x7a3...9F4c (Vlad Sovereign)")
        } else {
            _settings.value = _settings.value.copy(connectedWallet = null)
        }
    }
}
