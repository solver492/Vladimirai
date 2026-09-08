package com.ai.venice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ai.venice.ui.VeniceApp
import com.ai.venice.ui.theme.VeniceAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VeniceAITheme {
                VeniceApp()
            }
        }
    }
}
