package com.kotlinConf.moodtracker.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri


class ImageHandler(private val context: Context) {

    fun getResizedBitmapFromUri(uri: Uri): Bitmap? {

        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        var inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.use { BitmapFactory.decodeStream(it, null, options) }
        val imageWidth = options.outWidth
        val imageHeight = options.outHeight

        var inSampleSize = 1
        while (imageWidth / inSampleSize > 1000 || imageHeight / inSampleSize > 1000) {
            inSampleSize++
        }

        options.inSampleSize = inSampleSize
        options.inJustDecodeBounds = false

        inputStream = context.contentResolver.openInputStream(uri)
        return inputStream?.use { BitmapFactory.decodeStream(it, null, options) }
    }

}