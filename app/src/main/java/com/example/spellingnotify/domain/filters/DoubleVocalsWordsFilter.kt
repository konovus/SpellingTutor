package com.example.spellingnotify.domain.filters

data class DoubleVocalsWordsFilter(
    override val text: String = "Double vocals words",
    override var value: Boolean = true
): WordsFilter(text = text, value = value) {

    override fun filter(word: String): Boolean {
        val vocals = "aeiou"
        var result = false
        word.forEachIndexed { index, c ->
            if (index != 0 && word[index - 1] == c && vocals.contains(c)) {
                result = true
                return@forEachIndexed
            }
        }
        return result
    }
}