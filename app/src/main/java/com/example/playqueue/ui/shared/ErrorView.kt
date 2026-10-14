package com.example.playqueue.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * A Composable function that creates an error view
 */
@Composable
fun ErrorView() {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Something went wrong, please try again later.",
            modifier = Modifier.padding(20.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}