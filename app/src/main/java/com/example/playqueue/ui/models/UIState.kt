package com.example.playqueue.ui.models

/**
 * A sealed class that represents the UI state.
 */
sealed class UIState<out T> {
    object Loading: UIState<Nothing>()
    data class Success<T>(val data: T): UIState<T>()
    data class Error(val message: String, val error: Throwable? = null): UIState<Nothing>()
}