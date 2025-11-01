package ru.yandex.praktikumchatapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yandex.praktikumchatapp.data.ChatRepository

class ChatViewModel(
    val isWithReplies: Boolean = true
) : ViewModel() {

    private val repository = ChatRepository()

    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    init {
        viewModelScope.launch {
            while (isWithReplies) {
                repository.getReplyMessage()
                    .catch {
                        //TODO: Обработать ошибку если нужно
                    }
                    .collect { response ->
                        _chatState.update {
                            it.copy(
                                messages = it.messages + Message.OtherMessage(response),
                                shouldShowKeyboard = it.messages.isEmpty() && response.isNotEmpty()
                            )
                        }
                    }

            }
        }
    }

    fun sendMyMessage(messageText: String) {
        _chatState.update {
            it.copy(messages = it.messages + Message.MyMessage(messageText))
        }
    }
}