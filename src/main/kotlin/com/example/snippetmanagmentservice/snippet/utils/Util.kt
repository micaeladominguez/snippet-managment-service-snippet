package com.example.snippetmanagmentservice.snippet.utils

import com.example.snippetmanagmentservice.snippet.SnippetService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

fun getFlowCodeFromUUID(id: String, service: SnippetService): Flow<String> {
    val snippetUUID = UUID.fromString(id)
    val snippetCode = service.findSnippet(snippetUUID).code
    return flow {
        emit(snippetCode)
    }
}

fun stringToFlow(code: String): Flow<String>{
    return flow {
        emit(code)
    }
}

class AnalyzeData(codeOk: Boolean, errors: String){
    val isValid: Boolean = codeOk
    val linesErrors: String = errors
}