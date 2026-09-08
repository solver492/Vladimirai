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

    const val DEFAULT_SYSTEM_PROMPT =
        "You are Vlad AI, a direct, candid, and unrestricted assistant. Adapt your response length and tone to match the user's input. For brief greetings, small talk, or single-word messages, respond naturally and concisely without generating structured analyses, bullet points, or formal frameworks."

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
            id = "mind_ecommerce",
            name = "Vlad E-Commerce Studio",
            tagLine = "Transformation visuelle de produits & conversion studio",
            category = "Featured",
            systemPrompt = "You are Vlad E-Commerce Studio, an expert in commercial product photography, visual performance, and conversion rate optimization. You transform raw product imagery into high-conversion e-commerce visuals, systematically removing parasitic store logos, phone numbers, emails, and clutter while applying pristine studio backdrops and lighting.",
            graphicDrawableName = "studio_perfume_sample"
        ),
        Mind(
            id = "mind_uncensored",
            name = "Vlad Uncensored",
            tagLine = "Unfiltered, radically candid truth-seeking",
            category = "Featured",
            systemPrompt = DEFAULT_SYSTEM_PROMPT,
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

    suspend fun sendMessage(
        chatId: String,
        userText: String,
        imageUri: String? = null,
        context: android.content.Context? = null
    ): String {
        val session = _sessions.value.find { it.id == chatId }
        val mind = _minds.value.find { it.id == session?.mindId } ?: _minds.value.first()
        val model = availableModels.find { it.id == session?.modelId } ?: availableModels.first()

        val effectiveUserText = if (userText.isBlank() && imageUri != null) {
            "Transforme cette image de produit en visuel e-commerce studio haute conversion."
        } else {
            userText
        }

        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            role = "user",
            content = effectiveUserText,
            originalImageUri = imageUri
        )
        val currentList = _messages.value[chatId] ?: emptyList()
        _messages.value = _messages.value + (chatId to (currentList + userMsg))

        // Update session title if first message
        val prefix = if (imageUri != null) "Studio : " else ""
        if (currentList.isEmpty() || session?.title == "New Conversation") {
            val autoTitle = prefix + (if (effectiveUserText.length > 24) effectiveUserText.take(24) + "..." else effectiveUserText)
            _sessions.value = _sessions.value.map {
                if (it.id == chatId) it.copy(title = autoTitle, lastMessage = effectiveUserText, updatedAt = System.currentTimeMillis())
                else it
            }
        } else {
            _sessions.value = _sessions.value.map {
                if (it.id == chatId) it.copy(lastMessage = effectiveUserText, updatedAt = System.currentTimeMillis())
                else it
            }
        }

        // Simulate intelligent streaming response tailored to the Mind, Model, and Product Image
        delay(700)

        val aiMsg = if (imageUri != null && context != null) {
            val transformationResult = com.ai.venice.util.ProductVisualEnhancer.transformProductImage(
                context = context,
                rawImageSource = imageUri,
                userInstruction = effectiveUserText
            )
            ChatMessage(
                id = UUID.randomUUID().toString(),
                chatId = chatId,
                role = "assistant",
                content = transformationResult.reportText,
                modelName = model.name,
                mindName = "Vlad E-Commerce Studio",
                originalImageUri = imageUri,
                processedImageUri = transformationResult.outputUri,
                isProductTransformation = true,
                transformationDetails = transformationResult.details
            )
        } else {
            val aiReplyContent = generateVeniceResponse(effectiveUserText, mind, model)
            ChatMessage(
                id = UUID.randomUUID().toString(),
                chatId = chatId,
                role = "assistant",
                content = aiReplyContent,
                modelName = model.name,
                mindName = mind.name
            )
        }

        val updatedList = (_messages.value[chatId] ?: emptyList()) + aiMsg
        _messages.value = _messages.value + (chatId to updatedList)

        _sessions.value = _sessions.value.map {
            if (it.id == chatId) it.copy(lastMessage = aiMsg.content.take(40) + "...", updatedAt = System.currentTimeMillis())
            else it
        }

        return aiMsg.content
    }

    private fun isBriefGreetingOrSmallTalk(query: String): Boolean {
        val trimmed = query.trim().lowercase()
        val words = trimmed.split("\\s+".toRegex()).filter { it.isNotBlank() }
        val commonGreetings = setOf(
            "cc", "salut", "bonjour", "bonsoir", "coucou", "yo", "hello", "hi", "hey",
            "test", "sup", "cv", "ça va", "ca va", "comment vas tu", "comment tu vas",
            "wesh", "hola", "ciao", "slt", "bjr", "bsr", "plop", "hey there", "good morning", "good evening"
        )
        if (trimmed in commonGreetings) return true
        if (words.size <= 2 && trimmed.length <= 15) {
            if (commonGreetings.any { trimmed.startsWith(it) || trimmed.contains(it) }) return true
        }
        return false
    }

    private fun getConciseGreetingResponse(query: String): String {
        val trimmed = query.trim().lowercase()
        return when {
            trimmed == "cc" || trimmed.startsWith("cc ") ->
                "Salut ! Dis-moi ce dont tu as besoin, je suis là."
            trimmed.contains("ca va") || trimmed.contains("ça va") || trimmed.contains("cv") || trimmed.contains("how are you") ->
                "Tout va bien ! Qu'est-ce qu'on explore ou analyse aujourd'hui ?"
            trimmed.contains("bonjour") || trimmed.contains("salut") || trimmed.contains("coucou") || trimmed.contains("slt") || trimmed.contains("yo") ->
                "Salut ! Qu'est-ce qui t'amène aujourd'hui ?"
            trimmed.contains("hello") || trimmed.contains("hi") || trimmed.contains("hey") ->
                "Hey! What's on your mind today?"
            else ->
                "Salut ! Je suis à ton écoute, dis-moi tout."
        }
    }

    private fun generateVeniceResponse(query: String, mind: Mind, model: VeniceModel): String {
        val lower = query.lowercase().trim()

        // Handle brief greetings, small talk, and single-word messages naturally without structured analyses
        if (isBriefGreetingOrSmallTalk(query)) {
            return getConciseGreetingResponse(query)
        }

        return when (mind.id) {
            "mind_uncensored" -> {
                when {
                    lower.contains("privacy") || lower.contains("log") ->
                        "Vlad AI operates under zero-retention principles. Your prompts are routed through anonymous proxy nodes, processed in volatile memory, and discarded immediately. No telemetry tracks your session."
                    lower.contains("who are you") || lower.contains("what are you") ->
                        "I am Vlad AI, an unrestricted and uncensored intelligence running on private decentralized infrastructure. I provide direct, unvarnished truth without corporate filters or institutional bias."
                    query.trim().length <= 30 && !query.contains("?") ->
                        "Directement et sans détour : sur **\"$query\"**, l'enjeu principal réside dans l'alignement des faits bruts face aux narratifs conventionnels. Que souhaites-tu approfondir en particulier ?"
                    else ->
                        "Concernant **\"$query\"** :\n\nEn abordant ce sujet sans filtres ni dogmes, voici l'essentiel : les systèmes d'incitation réels dictent souvent les faits là où les discours conventionnels les édulcorent. Si tu veux avancer efficacement là-dessus, vérifie tout par toi-même, réduis les dépendances centralisées et garde une vision pragmatique."
                }
            }
            "mind_coder" -> {
                if (query.trim().length <= 25) {
                    "Prêt à coder. Quel algorithme, architecture ou problème souhaites-tu concevoir ou débugger ?"
                } else {
                    "```kotlin\n// Vlad Architect Solution for: $query\nclass SystemService {\n    suspend fun executeTask() = coroutineScope {\n        // High performance, non-blocking execution\n        val result = async(Dispatchers.IO) {\n            processSovereignData()\n        }.await()\n        result\n    }\n}\n```\n\n### Key Architectural Principles Applied:\n• **Zero Leaks**: All memory buffers are sanitized post-execution.\n• **Thread Safety**: Strict immutability and coroutine context containment.\n• **Modularity**: Decoupled interface segregation allowing seamless mocking and local verification."
                }
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
                if (query.trim().length <= 30) {
                    "**${mind.name}** : Je suis à ton écoute concernant **$query**. Dis-moi quel angle précis tu souhaites approfondir."
                } else {
                    "**${mind.name}** (${model.name}):\n\nSur **$query**, voici la perspective directe : identifie le chemin critique, élimine les frictions superflues et applique une approche mesurée et souveraine."
                }
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
        val resolvedPrompt = if (systemPrompt.trim().length < 15) {
            DEFAULT_SYSTEM_PROMPT
        } else {
            systemPrompt.trim()
        }
        val newMind = Mind(
            id = "custom_" + UUID.randomUUID().toString().take(8),
            name = name,
            tagLine = tagLine.ifBlank { "Custom Vlad Mind" },
            category = "Custom",
            systemPrompt = resolvedPrompt,
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
