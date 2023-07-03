package com.example.snippetmanagmentservice.snippet.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun stringToFlow(text: String): Flow<String> {
    return flow {
        emit(text)
    }
}