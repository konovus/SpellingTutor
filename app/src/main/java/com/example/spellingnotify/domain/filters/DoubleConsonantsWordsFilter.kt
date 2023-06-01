package com.example.spellingnotify.domain.filters

data class DoubleConsonantsWordsFilter(
    override val text: String = "Double consonants words",
    override var value: Boolean = true
): WordsFilter(text = text, value = value) {

    private val vocals = "aeiou"

    override fun filter(word: String): Boolean {
        var result = false
        word.forEachIndexed { index, c ->
            if (index != 0 && word[index - 1] == c && !vocals.contains(c)) {
                result = true
                return@forEachIndexed
            }
        }
        return result
    }
}