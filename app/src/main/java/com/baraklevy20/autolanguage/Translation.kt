package com.baraklevy20.autolanguage

data class Translation(val translatedWord: String,
                       val sourceSentences: MutableList<String>,
                       val targetSentences: MutableList<String>)