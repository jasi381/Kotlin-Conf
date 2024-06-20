package com.kotlinConf.moodtracker.viewModel

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.kotlinConf.moodtracker.model.ImageHandler
import com.kotlinConf.moodtracker.model.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoodDetectorViewModel @Inject constructor(
    private val imageHandler: ImageHandler,
    private val generativeModel: GenerativeModel
) : ViewModel() {

    // Image state holder
    var image by mutableStateOf<Bitmap?>(null)
        private set

    // Response state holder
    var responseState by mutableStateOf<ResponseState>(ResponseState.Initial)
        private set

    // Set image using provided Uri
    fun setImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            image = imageHandler.getResizedBitmapFromUri(uri)
        }
    }

    // Analyze mood from the stored image
    fun analyzeMood() {
        viewModelScope.launch(Dispatchers.IO) {
            responseState = ResponseState.Loading

            try {
                // Generate content using the generative model
                val response = generativeModel.generateContent(
                    content {
                        image(image ?: return@content)
                        text("Analyze the mood conveyed by the person in the attached image.")
                    }
                )
                // Set response state to success if text content is available
                response.text?.let { outputContent ->
                    responseState = ResponseState.Success(outputContent)
                }
            } catch (e: Exception) {
                // Set response state to error if an exception occurs
                responseState = ResponseState.Error(e.localizedMessage ?: "")
            }
        }
    }
}