package com.example.spellingnotify.domain.filters

import javax.inject.Inject

open class WordsFilter @Inject constructor(
    open val text: String,
    open var value: Boolean,
) {

    open fun filter(word: String): Boolean {
        return true
    }

    companion object {

        val allFilters = listOf(
            ThreeLetterWordsFilter(),
            FourLetterWordsFilter(),
            FiveLetterWordsFilter(),
            SixLetterWordsFilter(),
            SevenAndMoreLetterWordsFilter(),
            DoubleVocalsWordsFilter(),
            DoubleConsonantsWordsFilter(),
        )
    }
}