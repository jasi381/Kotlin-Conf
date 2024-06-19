package com.kotlinConf.moodtracker.view

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.kotlinConf.moodtracker.view.screens.MainScreen
import com.kotlinConf.moodtracker.view.theme.MoodTrackerTheme
import com.kotlinConf.moodtracker.viewModel.MoodDetectorViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            ),
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
        )
        setContent {
            MoodTrackerTheme(
                dynamicColor = false,
            ) {
                val viewModel: MoodDetectorViewModel = hiltViewModel()
                MainScreen(
                    selectedImage = viewModel.image,
                    responseState = viewModel.responseState,
                    onImageSelected = viewModel::setImage,
                    onAnalyzeMood = viewModel::analyzeMood
                )

            }
        }
    }
}

