package com.example.spellingnotify.domain.filters

data class FourLetterWordsFilter(
    override val text: String = "Four letter words",
    override var value: Boolean = true
): WordsFilter(text = text, value = value) {
    override fun filter(word: String): Boolean {
        return word.length == 4
    }
}