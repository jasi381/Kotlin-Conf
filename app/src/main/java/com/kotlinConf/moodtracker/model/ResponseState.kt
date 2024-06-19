package com.kotlinConf.moodtracker.model

interface ResponseState {
    data object Initial : ResponseState
    data object Loading : ResponseState

    data class Success(val outputText: String) : ResponseState
    data class Error(val errorMessage: String) : ResponseState
}