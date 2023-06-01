package com.example.spellingnotify.domain.filters

data class SevenAndMoreLetterWordsFilter(
    override val text: String = "Seven and more letter words",
    override var value: Boolean = true
): WordsFilter(text = text, value = value) {
    override fun filter(word: String): Boolean {
        return word.length >= 7
    }
}