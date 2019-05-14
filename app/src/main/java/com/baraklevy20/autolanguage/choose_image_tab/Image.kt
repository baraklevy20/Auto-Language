package com.baraklevy20.autolanguage.choose_image_tab

import android.graphics.Bitmap

data class Image(
    val title: String,
    val imageLink: String,
    val width: Int,
    var bitmap: Bitmap?
)