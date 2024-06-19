package com.kotlinConf.moodtracker.view.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commandiron.compose_loading.WanderingCubes
import com.kotlinConf.moodtracker.R
import com.kotlinConf.moodtracker.model.ResponseState
import dev.jeziellago.compose.markdowntext.MarkdownText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    selectedImage: Bitmap?,
    responseState: ResponseState,
    onImageSelected: (Uri) -> Unit,
    onAnalyzeMood: () -> Unit
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { imgUri ->
            imgUri?.let(onImageSelected)
        }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Mood Analyzer", fontFamily = FontFamily.Monospace)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(it)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            ElevatedCard(
                onClick = {
                    launcher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .aspectRatio(16 / 9f),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 8.dp
                ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImage != null) {
                        Image(
                            bitmap = selectedImage.asImageBitmap(),
                            contentDescription = "Person image",
                            modifier = Modifier
                                .fillMaxSize()
                                .aspectRatio(2 / 3f)
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "Pick Image Icon",
                                modifier = Modifier.size(32.dp)
                            )
                            Text(text = "Pick an image for mood analysis ")
                        }
                    }
                }
            }

            ElevatedButton(

                onClick = onAnalyzeMood,
                enabled = (responseState != ResponseState.Loading && selectedImage != null),
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .align(Alignment.CenterHorizontally),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_gemini),
                    contentDescription = "Face Icon"
                )
                Spacer(Modifier.width(8.dp))
                Text(text = "Analyze mood", fontSize = 16.sp, fontFamily = FontFamily.Monospace)
            }

            when (responseState) {
                ResponseState.Initial -> {}
                ResponseState.Loading -> {
                    WanderingCubes(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                is ResponseState.Success -> {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Surface(
                            shape = CircleShape,
                            modifier = Modifier.size(33.dp),
                            color = MaterialTheme.colorScheme.surface,
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_gemini),
                                contentDescription = "Gemini Icon",
                                modifier = Modifier
                                    .size(28.dp)
                                    .padding(5.dp)
                            )
                        }
                        AnimatedText(text = responseState.outputText)
                    }
                }

                is ResponseState.Error -> {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            Icons.Rounded.Warning,
                            tint = Color.Red,
                            contentDescription = "Error Icon"
                        )
                        Text(
                            text = responseState.errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedText(
    text: String,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    spec: AnimationSpec<Int> = tween(durationMillis = text.length * 50, easing = LinearEasing),
    style: TextStyle = LocalTextStyle.current,
    preoccupySpace: Boolean = true
) {
    var textToAnimate by remember { mutableStateOf("") }
    val index = remember { Animatable(initialValue = 0, typeConverter = Int.VectorConverter) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            textToAnimate = text
            index.animateTo(text.length, spec)
        } else {
            index.snapTo(0)
        }
    }

    LaunchedEffect(text) {
        if (isVisible) {
            index.snapTo(0)
            textToAnimate = text
            index.animateTo(text.length, spec)
        }
    }

    Box(modifier = modifier) {
        if (preoccupySpace && index.isRunning) {
            MarkdownText(
                markdown = text,
                style = style,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .alpha(0f),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )
        }
        MarkdownText(
            markdown = textToAnimate.substring(0, index.value),
            style = style,
            modifier = Modifier.padding(horizontal = 8.dp),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp
        )
    }
}