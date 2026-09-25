package com.example.playqueue.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EmptyView() {
    Column {
        Spacer(modifier = Modifier.height(30.dp))
    }
}

/**
 * A composable for the Inventory screen.
 */
@Composable
fun InventoryScreen() {
    EmptyView()
}