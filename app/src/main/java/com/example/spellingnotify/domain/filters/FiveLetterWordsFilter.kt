package com.example.spellingnotify.domain.filters

data class FiveLetterWordsFilter(
    override val text: String = "Five letter words",
    override var value: Boolean = true
): WordsFilter(text = text, value = value) {
    override fun filter(word: String): Boolean {
        return word.length == 5
    }
}