package com.example.spellingnotify.domain.filters

data class ThreeLetterWordsFilter(
    override val text: String = "Three letter words",
    override var value: Boolean = true
): WordsFilter(text = text, value = value) {
    override fun filter(word: String): Boolean {
        return word.length == 3
    }
}