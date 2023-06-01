package com.example.spellingnotify.domain.filters

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DoubleVocalsWordsFilterTest {

    private lateinit var doubleVocalsWordsFiler: FiveLetterWordsFilter

    @Before
    fun setup() {
        doubleVocalsWordsFiler = FiveLetterWordsFilter()
    }

    @Test
    fun `filter, empty word, returns false`() {
        val result = doubleVocalsWordsFiler.filter("")
        assertThat(result).isFalse()
    }

    @Test
    fun `filter, correct word, returns true`() {
        val result = doubleVocalsWordsFiler.filter("good")
        assertThat(result).isTrue()
    }

    @Test
    fun `filter, incorrect word, returns false`() {
        val result = doubleVocalsWordsFiler.filter("tenet")
        assertThat(result).isFalse()
    }
}