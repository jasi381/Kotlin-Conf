package com.kotlinConf.moodtracker.di

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import com.kotlinConf.moodtracker.BuildConfig
import com.kotlinConf.moodtracker.model.ImageHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideImageHandler(@ApplicationContext context: Context): ImageHandler {
        return ImageHandler(context)
    }

    @Provides
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.apikey
        )

    }

}