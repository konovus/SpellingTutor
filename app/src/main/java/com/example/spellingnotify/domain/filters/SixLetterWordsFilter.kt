package com.example.spellingnotify.domain.filters

data class SixLetterWordsFilter(
    override val text: String = "Six letter words",
    override var value: Boolean = true
): WordsFilter(text = text, value = value) {
    override fun filter(word: String): Boolean {
        return word.length == 6
    }
}