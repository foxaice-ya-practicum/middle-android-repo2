package ru.yandex.praktikumchatapp.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen
import kotlin.math.pow

class ChatRepository(
    private val api: ChatApi = ChatApi()
) {

    fun getReplyMessage(): Flow<String> {
        return api.getReply().retryWhen { cause, attempt ->
            if (attempt > MAX_RETRY_COUNT) return@retryWhen false

            delay((RETRY_DELAY_IN_MILLIS * 2.0.pow(attempt.toDouble())).toLong())
            return@retryWhen true
        }
    }

    private companion object {
        const val RETRY_DELAY_IN_MILLIS = 500L
        const val MAX_RETRY_COUNT = 3
    }
}