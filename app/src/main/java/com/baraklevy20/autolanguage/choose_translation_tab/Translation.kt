package com.baraklevy20.autolanguage.choose_translation_tab

data class Translation(val translatedWord: String,
                       val sourceSentences: MutableList<String>,
                       val targetSentences: MutableList<String>)