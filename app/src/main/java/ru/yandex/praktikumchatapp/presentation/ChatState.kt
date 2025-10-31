package ru.yandex.praktikumchatapp.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class ChatState(
    val messages: List<Message> = emptyList(),
    val shouldShowKeyboard: Boolean = false
)