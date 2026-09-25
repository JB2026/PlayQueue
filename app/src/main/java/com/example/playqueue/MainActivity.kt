package com.example.playqueue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.playqueue.ui.navigation.PlayQueueNavigationBar
import com.example.playqueue.ui.theme.PlayQueueTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlayQueueTheme {
                PlayQueueNavigationBar()
            }
        }
    }
}